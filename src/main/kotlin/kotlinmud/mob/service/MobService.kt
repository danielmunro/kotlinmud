package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlinmud.event.factory.createDeathEvent
import kotlinmud.event.factory.createKillEvent
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.helper.math.normalizeDouble
import kotlinmud.io.factory.createArriveMessage
import kotlinmud.io.factory.createFleeMessage
import kotlinmud.io.factory.createLeaveMessage
import kotlinmud.io.factory.createSingleHitMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Message
import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.service.CorpseService
import kotlinmud.item.service.ItemService
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.helper.takeDamageFromFall
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.respawn.helper.respawn
import kotlinmud.respawn.model.MobRespawn
import kotlinmud.room.model.Room
import kotlinmud.room.type.Area
import kotlinmud.room.type.Direction
import kotlinx.coroutines.runBlocking
import java.util.UUID

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService
) {
    private val logger = logger(this)
    private val fights = mutableListOf<Fight>()
    private val mobs = mutableListOf<Mob>()

    fun builder(
        name: String,
        brief: String,
        description: String,
        race: Race = Human(),
    ): MobBuilder {
        return MobBuilder(this).also {
            it.name = name
            it.brief = brief
            it.description = description
            it.race = race
        }
    }

    fun buildShopkeeper(
        name: String,
        brief: String,
        description: String,
        race: Race,
        room: Room,
        items: List<Pair<ItemBuilder, Int>>,
    ) {
        builder(
            name,
            brief,
            description,
            race
        ).also {
            it.room = room
            it.makeShopkeeper()
            itemRespawnsFor(it.canonicalId, items)
        }.build()
    }

    fun buildFodder(
        name: String,
        brief: String,
        description: String,
        race: Race,
        level: Int,
        area: Area,
        maxAmount: Int,
    ): MobBuilder {
        val builder = builder(
            name,
            brief,
            description,
            race,
        ).also {
            it.level = level
        }
        respawn(
            MobRespawn(
                builder,
                area,
                maxAmount,
            )
        )
        return builder
    }

    fun getMobCount(): Int {
        return mobs.size
    }

    suspend fun regenMobs() {
        findPlayerMobs().forEach {
            val baseRegen = getRoomRegenRate(it.room.regenLevel) + getDispositionRegenRate(it.disposition)
            val event = RegenEvent(it, baseRegen, baseRegen, baseRegen)
            eventService.publish(Event(EventType.REGEN, event))
            it.increaseByRegenRate(
                normalizeDouble(0.0, event.hpRegenRate, 1.0),
                normalizeDouble(0.0, event.manaRegenRate, 1.0),
                normalizeDouble(0.0, event.mvRegenRate, 1.0)
            )
        }
    }

    fun addMob(mob: Mob) {
        mobs.add(mob)
    }

    fun removeMob(mob: Mob) {
        mobs.remove(mob)
    }

    fun createMobController(mob: Mob): MobController {
        return MobController(this, eventService, mob)
    }

    fun addFight(mob1: Mob, mob2: Mob): FightService {
        val fight = Fight(mob1, mob2)
        fights.add(fight)
        mob1.disposition = Disposition.FIGHTING
        mob2.disposition = Disposition.FIGHTING
        return FightService(fight, eventService)
    }

    fun getMobFight(mob: Mob): Fight? {
        return fights.find { it.isParticipant(mob) }
    }

    fun findMobsInRoom(room: Room): List<Mob> {
        return mobs.filter { it.room == room }
    }

    fun findMob(search: (Mob) -> Boolean): Mob? {
        return mobs.find(search)
    }

    fun findPlayerMobs(): List<PlayerMob> {
        return mobs.filterIsInstance<PlayerMob>()
    }

    fun findPlayerMobsInArea(area: Area): List<PlayerMob> {
        return mobs.filter { it is PlayerMob && it.room.area == area } as List<PlayerMob>
    }

    fun findMobsByJobType(jobType: JobType): List<Mob> {
        return mobs.filter { it.job == jobType }
    }

    fun findMobsByCanonicalId(canonicalId: UUID): List<Mob> {
        return mobs.filter {
            it.canonicalId == canonicalId
        }
    }

    fun findMobsWantingToMoveOnTick(): List<Mob> {
        return mobs.filter {
            it.job == JobType.FODDER ||
                it.job == JobType.SCAVENGER ||
                it.job == JobType.PATROL
        }
    }

    suspend fun moveMob(mob: Mob, destinationRoom: Room, directionLeavingFrom: Direction) {
        val leaving = mob.room
        logger.debug("$mob leaves ${leaving.name}, entering ${destinationRoom.name}")
        sendMessageToRoom(createLeaveMessage(mob, directionLeavingFrom), leaving, mob)
        mob.room = destinationRoom
        doFallCheck(mob, leaving, destinationRoom)
        sendMessageToRoom(createArriveMessage(mob), destinationRoom, mob)
    }

    suspend fun proceedFights(): List<Round> {
        val rounds = createNewFightRounds()
        fights.removeIf { it.isOver() }
        return rounds
    }

    fun decrementAffects() {
        mobs.forEach {
            it.affects.removeIf { affect ->
                if (affect.timeout == null) {
                    return@removeIf false
                }
                affect.timeout = affect.timeout!! - 1
                affect.timeout!! <= 0
            }
        }
    }

    suspend fun pruneDeadMobs() {
        mobs.filter { it.disposition == Disposition.DEAD }.forEach {
            createCorpseFrom(it)
            eventService.publish(createDeathEvent(it))
            if (it is PlayerMob) {
                it.disposition = Disposition.SITTING
            }
        }
        mobs.removeIf {
            it !is PlayerMob && it.disposition == Disposition.DEAD
        }
    }

    suspend fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createCorpseFrom(mob: Mob): Item {
        return CorpseService(itemService).createCorpseFromMob(mob)
    }

    fun flee(mob: Mob) {
        getMobFight(mob)?.let {
            makeMobFlee(FightService(it, eventService), mob)
        } ?: logger.debug("flee :: no fight for mob :: {}", mob.name)
    }

    fun removeDecayedItems() {
        mobs.forEach {
            it.items.removeIf { item ->
                item.decayTimer != null && item.decayTimer!! <= 0
            }
        }
    }

    private fun makeMobFlee(fight: FightService, mob: Mob) {
        fight.end()
        val exit = mob.room.getAllExits().entries.random()
        runBlocking {
            sendMessageToRoom(
                createFleeMessage(mob, exit.key),
                mob.room,
                mob
            )
            mob.room = exit.value
            sendMessageToRoom(
                createArriveMessage(mob),
                exit.value,
                mob
            )
        }
    }

    private suspend fun createNewFightRounds(): List<Round> {
        return fights.mapNotNull {
            if (!it.isOver())
                proceedFightRound(FightService(it, eventService).createRound())
            else
                null
        }
    }

    private fun doFallCheck(mob: Mob, leaving: Room, arriving: Room) {
        (leaving.elevation - arriving.elevation).let {
            if (it > MAX_WALKABLE_ELEVATION) {
                takeDamageFromFall(mob, it)
            }
        }
    }

    private suspend fun proceedFightRound(round: Round): Round {
        val room = round.attacker.room
        sendRoundMessage(round.attackerAttacks, room, round.attacker, round.defender)
        sendRoundMessage(round.defenderAttacks, room, round.defender, round.attacker)
        eventService.publish(
            createSendMessageToRoomEvent(
                messageToActionCreator(round.defender.getHealthIndication()),
                room,
                round.attacker
            )
        )
        eventService.publish(
            createSendMessageToRoomEvent(
                messageToActionCreator(round.attacker.getHealthIndication()),
                room,
                round.defender
            )
        )
        if (round.hasFatality()) {
            eventService.publish(createKillEvent(round.fight))
        }
        return round
    }

    private suspend fun sendRoundMessage(attacks: List<Attack>, room: Room, attacker: Mob, defender: Mob) {
        attacks.forEach {
            val verb = if (it.attackResult == AttackResult.HIT) it.attackVerb else "miss"
            val verbPlural = if (it.attackResult == AttackResult.HIT) it.attackVerb.pluralize() else "misses"
            eventService.publish(
                createSendMessageToRoomEvent(
                    createSingleHitMessage(attacker, defender, verb, verbPlural),
                    room,
                    attacker,
                    defender
                )
            )
        }
    }
}
