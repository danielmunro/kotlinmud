package kotlinmud.mob

import kotlinmud.attributes.AttributesBuilder
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
        .wimpy(0)
        .savingThrows(0)
        .gold(0)
        .trains(0)
        .practices(0)
        .isNpc(true)
        .experiencePerLevel(1000)
        .skills(mutableMapOf())
        .affects(mutableListOf())
        .trainedAttributes(mutableListOf())
        .route(listOf())
        .attributes(
            AttributesBuilder()
                .hp(startingHp)
                .mana(startingMana)
                .mv(startingMv)
                .build()
        )
}
