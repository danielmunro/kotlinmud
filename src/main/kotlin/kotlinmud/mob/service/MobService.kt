package kotlinmud.mob.service

import com.cesarferreira.pluralize.pluralize
import kotlin.math.roundToInt
import kotlinmud.affect.repository.decrementAffectsTimeout
import kotlinmud.affect.repository.deleteTimedOutAffects
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createFightRoundEvent
import kotlinmud.event.factory.createKillEvent
import kotlinmud.event.factory.createSendMessageToRoomEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.helper.math.normalizeDouble
import kotlinmud.io.factory.createArriveMessage
import kotlinmud.io.factory.createDeathMessage
import kotlinmud.io.factory.createFleeMessage
import kotlinmud.io.factory.createLeaveMessage
import kotlinmud.io.factory.createSingleHitMessage
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Message
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.mob.constant.MAX_WALKABLE_ELEVATION
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.helper.getDispositionRegenRate
import kotlinmud.mob.helper.getRoomRegenRate
import kotlinmud.mob.helper.takeDamageFromFall
import kotlinmud.mob.repository.findDeadMobs
import kotlinmud.mob.repository.findPlayerMobs
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.helper.getLearningDifficultyPracticeAmount
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.SpecializationType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction
import org.jetbrains.exposed.sql.transactions.transaction

class MobService(
    private val itemService: ItemService,
    private val eventService: EventService
) {
    private val fights = mutableListOf<Fight>()
    private val logger = logger(this)
    private val skills = createSkillList()

    fun regenMobs() {
        findPlayerMobs().forEach {
            it.increaseByRegenRate(
                normalizeDouble(
                    0.0,
                    getRoomRegenRate(it.room.regenLevel) +
                            getDispositionRegenRate(it.disposition),
                    1.0
                )
            )
        }
    }

    fun addFight(fight: Fight) {
        fights.add(fight)
    }

    fun findFightForMob(mob: MobDAO): Fight? {
        return fights.find { it.isParticipant(mob) }
    }

    fun moveMob(mob: MobDAO, destinationRoom: RoomDAO, directionLeavingFrom: Direction) {
        val leaving = transaction { mob.room }
        sendMessageToRoom(createLeaveMessage(mob, directionLeavingFrom), leaving, mob)
        transaction { mob.room = destinationRoom }
        doFallCheck(mob, leaving, destinationRoom)
        sendMessageToRoom(createArriveMessage(mob), destinationRoom, mob)
    }

    fun proceedFights(): List<Round> {
        return createNewFightRounds().also {
            fights.filter { it.hasFatality() }.forEach {
                eventService.publish(createKillEvent(it))
                fights.remove(it)
            }
        }
    }

    fun decrementAffects() {
        transaction {
            deleteTimedOutAffects()
            decrementAffectsTimeout()
        }
    }

    fun pruneDeadMobs() {
        findDeadMobs().forEach {
            with(createCorpseFrom(it)) {
                transaction {
                    room = it.room
                    it.delete()
                    eventService.publishRoomMessage(
                        createSendMessageToRoomEvent(createDeathMessage(it), it.room, it)
                    )
                }
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
        fights.find { it.isParticipant(mob) }?.let {
            it.end()
            transaction {
                mob.room.getAllExits().entries.random().let { exit ->
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
        return fights.map {
            proceedFightRound(it.createRound()).also { round ->
                eventService.publish(createFightRoundEvent(round))
            }
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
