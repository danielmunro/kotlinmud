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
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.helper.takeDamageFromFall
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.specialization.helper.createSpecializationList
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.MobCanonicalId
import kotlinmud.room.model.Room
import kotlinmud.room.type.Direction
import kotlinx.coroutines.runBlocking

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService
) {
    private val logger = logger(this)
    private val fights = mutableListOf<Fight>()
    private val mobs = mutableListOf<Mob>()
    private val specializations = createSpecializationList()

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

    fun mapFromDAO(dao: MobDAO): Mob {
        return MobBuilder(this)
            .apply {
                name = dao.name
                brief = dao.brief
                description = dao.description
                hp = dao.hp
                mana = dao.mana
                mv = dao.mv
                level = dao.level
                race = createRaceFromString(dao.race)
                specialization = specializations.find { it.type == dao.specialization }
                disposition = dao.disposition
                gender = dao.gender
                wimpy = dao.wimpy
            }
            .build()
    }

    fun addMob(mob: Mob) {
        mobs.add(mob)
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

    fun findPlayerMobs(): List<PlayerMob> {
        return mobs.filterIsInstance<PlayerMob>()
    }

    fun findPlayerMobByName(name: String): PlayerMob? {
        return mobs.find { it is PlayerMob && it.name == name } as PlayerMob?
    }

    fun findMobsByJobType(jobType: JobType): List<Mob> {
        return mobs.filter { it.job == jobType }
    }

    fun findMobsByCanonicalId(canonicalId: MobCanonicalId): List<Mob> {
        return mobs.filter {
            it.canonicalId == canonicalId
        }
    }

    fun findMobsWantingToMoveOnTick(): List<Mob> {
        return mobs.filter {
            it.dao == null && (
                it.job == JobType.FODDER ||
                    it.job == JobType.SCAVENGER ||
                    it.job == JobType.PATROL
                )
        }
    }

    suspend fun moveMob(mob: Mob, destinationRoom: Room, directionLeavingFrom: Direction) {
        val leaving = mob.room
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
        }
        mobs.removeIf {
            it.dao == null && it.disposition == Disposition.DEAD
        }
    }

    suspend fun sendMessageToRoom(message: Message, room: Room, actionCreator: Mob, target: Mob? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createCorpseFrom(mob: Mob): Item {
        return itemService.createCorpseFromMob(mob)
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
