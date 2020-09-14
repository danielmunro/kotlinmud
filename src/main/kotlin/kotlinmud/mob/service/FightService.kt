package kotlinmud.mob.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createFightRoundEvent as createFightRoundEventFactory
import kotlinmud.event.factory.createFightStartedEvent as createFightStartedEventFactory
import kotlinmud.event.factory.createKillEvent as createKillEventFactory
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightRoundEvent
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.math.d20
import kotlinmud.helper.math.dice
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.type.Position
import kotlinmud.mob.dao.FightDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.fight.type.FightStatus
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class FightService(private val fight: FightDAO, private val eventService: EventService) {
    companion object {
        fun create(mob1: MobDAO, mob2: MobDAO, eventService: EventService): FightService {
            return FightService(transaction {
                mob1.disposition = Disposition.FIGHTING
                mob2.disposition = Disposition.FIGHTING
                FightDAO.new {
                    this.mob1 = mob1
                    this.mob2 = mob2
                }
            }, eventService)
        }

        private fun getAc(defender: MobDAO, damageType: DamageType): Int {
            return when (damageType) {
                DamageType.SLASH -> defender.calc(Attribute.AC_SLASH)
                DamageType.POUND -> defender.calc(Attribute.AC_BASH)
                DamageType.PIERCE -> defender.calc(Attribute.AC_PIERCE)
                else -> defender.calc(Attribute.AC_MAGIC)
            }
        }

        fun calculateDamage(attacker: MobDAO): Int {
            val hit = attacker.calc(Attribute.HIT)
            val dam = attacker.calc(Attribute.DAM)

            return dice(hit, dam) + dam
        }

        private fun rollEvasiveSkills(mob: MobDAO): SkillType? {
            val shield = mob.getEquippedByPosition(Position.SHIELD)
            val shieldBlock = mob.getSkill(SkillType.SHIELD_BLOCK)
            if (shield != null && shieldBlock != null && percentRoll() < transaction { shieldBlock.level } / 3) {
                return SkillType.SHIELD_BLOCK
            }

            val weapon = mob.getEquippedByPosition(Position.WEAPON)
            val parry = mob.getSkill(SkillType.PARRY)
            if (weapon != null && parry != null && percentRoll() < transaction { parry.level } / 3) {
                return SkillType.PARRY
            }

            val dodge = mob.getSkill(SkillType.DODGE)
            if (dodge != null && percentRoll() < transaction { dodge.level } / 3) {
                return SkillType.DODGE
            }

            return null
        }

        private fun applyRoundDamage(attacks: List<Attack>, mob: MobDAO) {
            transaction {
                attacks.forEach {
                    if (it.attackResult == AttackResult.HIT) {
                        mob.hp -= it.damage
                        mob.disposition = Disposition.FIGHTING
                    }
                }
                if (mob.hp < 0) {
                    mob.disposition = Disposition.DEAD
                }
            }
        }
    }

    fun end() {
        fight.status = FightStatus.OVER
        resetDisposition(fight.mob1)
        resetDisposition(fight.mob2)
    }

    fun makeMobFlee(mobId: Int, room: RoomDAO) {
        if (mobId == fight.mob1.id.value) {
            fight.mob1.room = room
        } else if (mobId == fight.mob2.id.value) {
            fight.mob2.room = room
        }
    }

    fun createFightStartedEvent(): Event<FightStartedEvent> {
        return transaction { createFightStartedEventFactory(fight, fight.mob1, fight.mob2) }
    }

    fun createKillEvent(): Event<KillEvent> {
        return createKillEventFactory(fight)
    }

    fun createFightRoundEvent(): Event<Round> {
        return createFightRoundEventFactory(
            Round(
                fight,
                fight.mob1,
                fight.mob2,
                mapAttacks(fight.mob1, fight.mob2),
                mapAttacks(fight.mob2, fight.mob1)
            )
        )
    }

    fun isOver(): Boolean {
        return fight.isOver()
    }

    fun createRound(): Round {
        val round = transaction {
            Round(
                fight,
                fight.mob1,
                fight.mob2,
                mapAttacks(fight.mob1, fight.mob2),
                mapAttacks(fight.mob2, fight.mob1)
            )
        }
        eventService.publish(FightRoundEvent(round))
        transaction { applyRoundDamage(round.attackerAttacks, round.defender) }
        if (round.isActive()) {
            applyRoundDamage(round.defenderAttacks, round.attacker)
        }
        transaction {
            if (fight.mob1.hp < 0) {
                fight.mob1.disposition = Disposition.DEAD
                fight.status = FightStatus.OVER
            } else if (fight.mob2.hp < 0) {
                fight.mob2.disposition = Disposition.DEAD
                fight.status = FightStatus.OVER
            }
        }
        return round
    }

    private fun resetDisposition(mob: MobDAO) {
        if (mob.disposition == Disposition.FIGHTING) {
            mob.disposition = Disposition.STANDING
        }
    }

    private fun mapAttacks(attacker: MobDAO, defender: MobDAO): MutableList<Attack> {
        return attacker.getAttacks().map {
            val skillType = rollEvasiveSkills(defender)
            when {
                skillType != null -> Attack(AttackResult.EVADE, attacker.getAttackVerb(), 0, attacker.getDamageType(), skillType)
                attackerDefeatsDefenderAC(attacker, defender) -> Attack(
                    AttackResult.HIT,
                    attacker.getAttackVerb(),
                    calculateDamage(attacker),
                    attacker.getDamageType()
                )
                else -> Attack(AttackResult.MISS, attacker.getAttackVerb(), 0, attacker.getDamageType())
            }
        }.toMutableList()
    }

    private fun attackerDefeatsDefenderAC(attacker: MobDAO, defender: MobDAO): Boolean {
        val roll = d20()
        val hit = attacker.calc(Attribute.HIT)
        val ac = getAc(defender, attacker.getDamageType())
        return roll - hit + ac < 0
    }
}
