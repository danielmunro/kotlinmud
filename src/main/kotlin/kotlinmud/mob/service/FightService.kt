package kotlinmud.mob.service

import kotlinmud.attributes.type.Attribute
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.KillEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.helper.math.d20
import kotlinmud.helper.math.percentRoll
import kotlinmud.item.type.Position
import kotlinmud.mob.factory.createEvadeAttack
import kotlinmud.mob.factory.createHitAttack
import kotlinmud.mob.factory.createMissAttack
import kotlinmud.mob.fight.Attack
import kotlinmud.mob.fight.Round
import kotlinmud.mob.fight.type.AttackResult
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Disposition
import kotlin.math.ceil
import kotlinmud.event.factory.createFightRoundEvent as createFightRoundEventFactory
import kotlinmud.event.factory.createFightStartedEvent as createFightStartedEventFactory
import kotlinmud.event.factory.createKillEvent as createKillEventFactory

class FightService(private val fight: Fight, private val eventService: EventService) {
    companion object {
        private fun getDispositionModifier(disposition: Disposition): Int {
            return when (disposition) {
                Disposition.FIGHTING -> 0
                Disposition.STANDING -> 1
                Disposition.SITTING -> 4
                Disposition.SLEEPING -> 9
                Disposition.DEAD -> 0
            }
        }

        private fun getDexModifier(dex: Int): Int {
            return ceil(dex / 10.0).toInt()
        }

        private fun getClassModifier(specializationType: SpecializationType?): Int {
            return when (specializationType) {
                SpecializationType.CLERIC,
                SpecializationType.MAGE -> 1
                SpecializationType.THIEF -> 2
                SpecializationType.WARRIOR -> 3
                else -> 0
            }
        }

        private fun getAc(defender: Mob, damageType: DamageType): Int {
            return when (damageType) {
                DamageType.SLASH -> defender.calc(Attribute.AC_SLASH)
                DamageType.POUND -> defender.calc(Attribute.AC_BASH)
                DamageType.PIERCE -> defender.calc(Attribute.AC_PIERCE)
                else -> defender.calc(Attribute.AC_MAGIC)
            }
        }

        private fun rollEvasiveSkills(mob: Mob): SkillType? {
            val shield = mob.getEquippedByPosition(Position.SHIELD)
            val shieldBlock = mob.skills[SkillType.SHIELD_BLOCK]
            if (shield != null && shieldBlock != null && percentRoll() < shieldBlock / 3) {
                return SkillType.SHIELD_BLOCK
            }

            val weapon = mob.getEquippedByPosition(Position.WEAPON)
            val parry = mob.skills[SkillType.PARRY]
            println(weapon != null)
            println(parry != null)
            if (parry != null) {
                println(percentRoll() < parry / 3)
            }
            if (weapon != null && parry != null && percentRoll() < parry / 3) {
                return SkillType.PARRY
            }

            val dodge = mob.skills[SkillType.DODGE]
            if (dodge != null && percentRoll() < dodge / 3) {
                return SkillType.DODGE
            }

            return null
        }

        private fun applyRoundDamage(attacks: List<Attack>, mob: Mob) {
            attacks.forEach {
                if (it.attackResult == AttackResult.HIT) {
                    mob.hp -= it.damage
                }
            }
            if (mob.hp < 0) {
                mob.disposition = Disposition.DEAD
            }
        }
    }

    private val logger = logger(this)

    fun end() {
        fight.finish()
        resetDisposition(fight.mob1)
        resetDisposition(fight.mob2)
    }

    fun createFightStartedEvent(): Event<FightStartedEvent> {
        return createFightStartedEventFactory(fight, fight.mob1, fight.mob2)
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

    suspend fun createRound(): Round {
        val attacks1 = mapAttacks(fight.mob1, fight.mob2)
        val attacks2 = if (attacks1.fold(0) { acc, attack -> acc + attack.damage } < fight.mob2.hp) {
            mapAttacks(fight.mob2, fight.mob1)
        } else mutableListOf()
        val round = Round(
            fight,
            fight.mob1,
            fight.mob2,
            attacks1,
            attacks2
        )
        eventService.publish(createFightRoundEventFactory(round))
        applyRoundDamage(round.attackerAttacks, round.defender)
        if (round.isActive()) {
            applyRoundDamage(round.defenderAttacks, round.attacker)
        }
        if (fight.mob1.hp < 0) {
            fight.mob1.disposition = Disposition.DEAD
            fight.mob2.disposition = Disposition.STANDING
            fight.finish()
        } else if (fight.mob2.hp < 0) {
            fight.mob1.disposition = Disposition.STANDING
            fight.mob2.disposition = Disposition.DEAD
            fight.finish()
        }
        return round
    }

    private fun resetDisposition(mob: Mob) {
        if (mob.disposition == Disposition.FIGHTING) {
            mob.disposition = Disposition.STANDING
        }
    }

    private fun mapAttacks(attacker: Mob, defender: Mob): MutableList<Attack> {
        val skillType = rollEvasiveSkills(defender)
        return mutableListOf(
            when {
                skillType != null -> createEvadeAttack(attacker, skillType)
                attackerDefeatsDefenderAC(attacker, defender) -> createHitAttack(attacker)
                else -> createMissAttack(attacker)
            }
        )
    }

    private fun attackerDefeatsDefenderAC(attacker: Mob, defender: Mob): Boolean {
        val roll = d20()

        logger.debug("roll value -- {}", roll)

        // bam
        if (roll == 20) {
            return true
        }

        if (roll == 1) {
            return false
        }

        val attackCalc = roll +
            attacker.calc(Attribute.HIT) +
            getDispositionModifier(attacker.disposition) +
            getClassModifier(attacker.specialization?.type) +
            getDexModifier(attacker.calc(Attribute.DEX))

        val defenseCalc = getAc(defender, attacker.getDamageType()) +
            getDispositionModifier(defender.disposition) +
            getClassModifier(defender.specialization?.type) +
            getDexModifier(defender.calc(Attribute.DEX))

        logger.debug(
            "result -- {}, attackCalc -- {}, defenseCalc -- {}",
            attackCalc > defenseCalc,
            attackCalc,
            defenseCalc,
        )

        return attackCalc > defenseCalc
    }
}
