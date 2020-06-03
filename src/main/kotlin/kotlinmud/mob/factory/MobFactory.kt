package kotlinmud.mob.factory

import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.model.startingHp
import kotlinmud.attributes.model.startingMana
import kotlinmud.attributes.model.startingMv
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
