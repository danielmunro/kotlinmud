package kotlinmud.mob

import kotlinmud.random.d20
import kotlinmud.random.dN

class Fight(private val mob1: MobEntity, private val mob2: MobEntity) {
    fun createRound(): Round {
        return Round(
            mob1,
            mob2,
            mapAttacks(mob1, mob2),
            mapAttacks(mob2, mob1))
    }

    private fun mapAttacks(attacker: MobEntity, defender: MobEntity): List<Attack> {
        return attacker.getAttacks().map {
            if (attackerDefeatsDefenderAC(attacker, defender)) {
                Attack(AttackResult.HIT, calculateDamage(attacker), attacker.getDamageType())
            }
            Attack(AttackResult.MISS, 0, mob1.getDamageType())
        }
    }

    private fun calculateDamage(attacker: MobEntity): Int {
        // hardcoded value for now, replace with weapon values
        return dN(1, 5) + attacker.attributes.dam
    }

    private fun attackerDefeatsDefenderAC(attacker: MobEntity, defender: MobEntity): Boolean {
        return d20() - attacker.attributes.hit + getAc(defender, attacker.getDamageType()) < 0
    }

    private fun getAc(defender: MobEntity, damageType: DamageType): Int {
        return when(damageType) {
            DamageType.SLASH -> defender.attributes.acSlash
            DamageType.POUND -> defender.attributes.acBash
            DamageType.PIERCE -> defender.attributes.acPierce
            else -> defender.attributes.acMagic
        }
    }
}
