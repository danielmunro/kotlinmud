package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlinmud.affect.repository.decrementAffectsTimeout
import kotlinmud.affect.repository.deleteTimedOutAffects
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
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
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
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
import kotlinmud.mob.repository.deleteFinishedFights
import kotlinmud.mob.skill.helper.getLearningDifficultyPracticeAmount
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.JobType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.roundToInt

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService,
    private val skills: List<Skill>
) {
    private val logger = logger(this)
    private val fights = mutableListOf<Fight>()
    private val mobs = mutableListOf<Mob>()

    suspend fun regenMobs() {
        findPlayerMobs().forEach {
            val baseRegen = transaction { getRoomRegenRate(it.room.regenLevel) + getDispositionRegenRate(it.disposition) }
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

    fun createMobController(mob: Mob): MobController {
        return MobController(this, itemService, eventService, mob)
    }

    fun addFight(mob1: Mob, mob2: Mob): FightService {
        val fight = Fight(mob1, mob2)
        fights.add(fight)
        return FightService(fight, eventService)
    }

    fun getMobFight(mob: Mob): Fight? {
        return fights.find { it.isParticipant(mob) }
    }

    fun findMobsInRoom(room: RoomDAO): List<Mob> {
        return mobs.filter { it.room == room }
    }

    fun findPlayerMobs(): List<Mob> {
        return mobs.filter { it.mobCard != null }
    }

    fun findMobsByJobType(jobType: JobType): List<Mob> {
        return mobs.filter { it.job == jobType }
    }

    fun findPlayerMobByName(name: String): Mob? {
        return mobs.find {
            it.mobCard != null && it.name == name
        }
    }

    fun findMobsWantingToMoveOnTick(): List<Mob> {
        return mobs.filter {
            it.mobCard == null && (
                it.job == JobType.FODDER ||
                    it.job == JobType.SCAVENGER ||
                    it.job == JobType.PATROL
                )
        }
    }

    suspend fun moveMob(mob: Mob, destinationRoom: RoomDAO, directionLeavingFrom: Direction) {
        val leaving = mob.room
        sendMessageToRoom(createLeaveMessage(mob, directionLeavingFrom), leaving, mob)
        transaction { mob.room = destinationRoom }
        doFallCheck(mob, leaving, destinationRoom)
        sendMessageToRoom(createArriveMessage(mob), destinationRoom, mob)
    }

    suspend fun proceedFights(): List<Round> {
        val rounds = createNewFightRounds()
        deleteFinishedFights()
        return rounds
    }

    fun decrementAffects() {
        transaction {
            deleteTimedOutAffects()
            decrementAffectsTimeout()
        }
    }

    suspend fun pruneDeadMobs() {
        mobs.filter { it.disposition == Disposition.DEAD }.forEach {
            createCorpseFrom(it)
            eventService.publish(createDeathEvent(it))
        }
        mobs.removeIf {
            it.mobCard == null && it.disposition == Disposition.DEAD
        }
    }

    suspend fun sendMessageToRoom(message: Message, room: RoomDAO, actionCreator: Mob, target: Mob? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createCorpseFrom(mob: Mob): ItemDAO {
        return itemService.createCorpseFromMob(mob)
    }

    fun flee(mob: Mob) {
        getMobFight(mob)?.let {
            makeMobFlee(FightService(it, eventService), mob)
        } ?: logger.debug("flee :: no fight for mob :: {}", mob.name)
    }

    fun train(card: MobCardDAO, attribute: Attribute) {
        transaction {
            card.trains -= 1
            val attributes = AttributesDAO.new {
                mobCard = card
            }
            attributes.setAttribute(attribute, if (attribute.isVitals()) 10 else 1)
        }
    }

    fun practice(mob: Mob, type: SkillType) {
        transaction {
            mob.mobCard!!.practices -= 1
            mob.skills[type] = mob.skills[type]!! + calculatePracticeGain(mob, type)
        }
    }

    fun decreaseThirstAndHunger(mobName: String): MobCardDAO? {
        return findMobCardByName(mobName)?.also {
            transaction {
                it.hunger -= 1
                it.thirst -= 1
            }
        }
    }

    private fun makeMobFlee(fight: FightService, mob: Mob) {
        fight.end()
        transaction {
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
    }

    private fun calculatePracticeGain(mob: Mob, type: SkillType): Int {
        return with(
            1 + getLearningDifficultyPracticeAmount(
                getSkillDifficultyForSpecialization(type, mob.specialization?.type ?: SpecializationType.NONE)
            )
        ) {
            (Math.random() * this + mob.calc(Attribute.INT) / 5).roundToInt()
        }
    }

    private fun getSkillDifficultyForSpecialization(type: SkillType, specialization: SpecializationType?): LearningDifficulty {
        return findSkillByType(type).difficulty[specialization] ?: LearningDifficulty.VERY_HARD
    }

    private fun findSkillByType(type: SkillType): Skill {
        return skills.find { it.type == type }!!
    }

    private suspend fun createNewFightRounds(): List<Round> {
        println("fights: ${fights.size}")
        return fights.map {
            proceedFightRound(FightService(it, eventService).createRound())
        }
    }

    private fun doFallCheck(mob: Mob, leaving: RoomDAO, arriving: RoomDAO) {
        (leaving.elevation - arriving.elevation).let {
            if (it > MAX_WALKABLE_ELEVATION) {
                takeDamageFromFall(mob, it)
            }
        }
    }

    private suspend fun proceedFightRound(round: Round): Round {
        val room = transaction { round.attacker.room }
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

    private suspend fun sendRoundMessage(attacks: List<Attack>, room: RoomDAO, attacker: Mob, defender: Mob) {
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
