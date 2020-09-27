package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlin.math.roundToInt
import kotlinmud.affect.repository.decrementAffectsTimeout
import kotlinmud.affect.repository.deleteTimedOutAffects
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
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
import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.helper.takeDamageFromFall
import kotlinmud.mob.repository.deleteFinishedFights
import kotlinmud.mob.repository.findDeadMobs
import kotlinmud.mob.repository.findFightForMob
import kotlinmud.mob.repository.findFights
import kotlinmud.mob.repository.findMobById
import kotlinmud.mob.repository.findPlayerMobs
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.helper.getLearningDifficultyPracticeAmount
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.repository.findMobCardByName
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService,
    private val skills: List<Skill>
) {
    private val logger = logger(this)

    fun regenMobs() {
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

    fun createMobController(mob: MobDAO): MobController {
        return MobController(this, itemService, eventService, mob)
    }

    fun addFight(mob1: MobDAO, mob2: MobDAO): FightService {
        return FightService.create(mob1, mob2, eventService)
    }

    fun getMobFight(mob: MobDAO): FightDAO? {
        return findFightForMob(mob)
    }

    fun moveMob(mob: MobDAO, destinationRoom: RoomDAO, directionLeavingFrom: Direction) {
        val leaving = transaction { mob.room }
        sendMessageToRoom(createLeaveMessage(mob, directionLeavingFrom), leaving, mob)
        transaction { mob.room = destinationRoom }
        doFallCheck(mob, leaving, destinationRoom)
        sendMessageToRoom(createArriveMessage(mob), destinationRoom, mob)
    }

    fun proceedFights(): List<Round> {
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

    fun pruneDeadMobs() {
        findDeadMobs().forEach {
            createCorpseFrom(it)
            transaction {
                eventService.publishDeath(it)
                it.delete()
            }
        }
    }

    fun sendMessageToRoom(message: Message, room: RoomDAO, actionCreator: MobDAO, target: MobDAO? = null) {
        eventService.publish(createSendMessageToRoomEvent(message, room, actionCreator, target))
    }

    fun createCorpseFrom(mob: MobDAO): ItemDAO {
        return itemService.createCorpseFromMob(mob)
    }

    fun flee(mob: MobDAO) {
        getMobFight(mob)?.let {
            makeMobFlee(FightService(it, eventService), mob)
        } ?: logger.debug("flee :: no fight for mob :: {}", mob.id)
    }

    fun transferGold(src: MobDAO, dst: MobDAO, amount: Int = src.gold) {
        transaction {
            src.gold -= amount
            dst.gold += amount
        }
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

    fun practice(mob: MobDAO, skill: SkillDAO) {
        transaction {
            mob.mobCard!!.practices -= 1
            skill.level += calculatePracticeGain(mob, skill)
        }
    }

    fun getMob(id: Int): MobDAO {
        return findMobById(id)
    }

    fun decreaseThirstAndHunger(mobName: String): MobCardDAO? {
        return findMobCardByName(mobName)?.also {
            it.hunger -= 1
            it.thirst -= 1
        }
    }

    private fun makeMobFlee(fight: FightService, mob: MobDAO) {
        fight.end()
        val exit = mob.room.getAllExits().entries.random()
        sendMessageToRoom(
            createFleeMessage(mob, exit.key),
            mob.room,
            mob
        )
        fight.makeMobFlee(mob.id.value, exit.value)
        sendMessageToRoom(
            createArriveMessage(mob),
            exit.value,
            mob
        )
    }

    private fun calculatePracticeGain(mob: MobDAO, skill: SkillDAO): Int {
        return with(1 + getLearningDifficultyPracticeAmount(
            getSkillDifficultyForSpecialization(skill.type, mob.specialization))
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

    private fun createNewFightRounds(): List<Round> {
        return findFights().map {
            proceedFightRound(FightService(it, eventService).createRound())
        }
    }

    private fun doFallCheck(mob: MobDAO, leaving: RoomDAO, arriving: RoomDAO) {
        (leaving.elevation - arriving.elevation).let {
            if (it > MAX_WALKABLE_ELEVATION) {
                takeDamageFromFall(mob, it)
            }
        }
    }

    private fun proceedFightRound(round: Round): Round {
        val room = transaction { round.attacker.room }
        sendRoundMessage(round.attackerAttacks, room, round.attacker, round.defender)
        sendRoundMessage(round.defenderAttacks, room, round.defender, round.attacker)
        eventService.publishRoomMessage(
            createSendMessageToRoomEvent(
                messageToActionCreator(round.defender.getHealthIndication()),
                room,
                round.attacker
            )
        )
        eventService.publishRoomMessage(
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

    private fun sendRoundMessage(attacks: List<Attack>, room: RoomDAO, attacker: MobDAO, defender: MobDAO) {
        attacks.forEach {
            val verb = if (it.attackResult == AttackResult.HIT) it.attackVerb else "miss"
            val verbPlural = if (it.attackResult == AttackResult.HIT) it.attackVerb.pluralize() else "misses"
            eventService.publishRoomMessage(
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
