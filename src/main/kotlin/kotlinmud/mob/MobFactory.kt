package kotlinmud.mob

import kotlinmud.attributes.Attributes
import kotlinmud.attributes.startingHp
import kotlinmud.attributes.startingMana
import kotlinmud.attributes.startingMv
import kotlinmud.item.Inventory
import kotlinmud.mob.race.impl.Human

fun mobBuilder(id: Int, name: String): MobBuilder {
    return MobBuilder()
        .id(id)
        .name(name)
        .hp(startingHp)
        .mana(startingMana)
        .mv(startingMv)
        .level(1)
        .race(Human())
        .disposition(Disposition.STANDING)
        .specialization(SpecializationType.NONE)
        .job(JobType.NONE)
        .gender(Gender.NONE)
        .inventory(Inventory())
        .equipped(Inventory())
        .attributes(
            Attributes.Builder()
                .setHp(startingHp)
                .setMana(startingMana)
                .setMv(startingMv)
                .build()
        )
}
