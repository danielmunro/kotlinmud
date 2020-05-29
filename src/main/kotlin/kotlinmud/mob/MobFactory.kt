package kotlinmud.mob

import kotlinmud.attributes.AttributesBuilder
import kotlinmud.attributes.startingHp
import kotlinmud.attributes.startingMana
import kotlinmud.attributes.startingMv
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.impl.Human

fun mobBuilder(id: Int, name: String): MobBuilder {
    return MobBuilder()
        .id(id)
        .name(name)
        .hp(startingHp)
        .mana(startingMana)
        .mv(startingMv)
        .race(Human())
        .level(1)
        .attributes(
            AttributesBuilder()
                .hp(startingHp)
                .mana(startingMana)
                .mv(startingMv)
                .build()
        )
}
