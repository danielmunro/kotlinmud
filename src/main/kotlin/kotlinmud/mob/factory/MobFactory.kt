package kotlinmud.mob.factory

import Horse
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.model.startingHp
import kotlinmud.attributes.model.startingMana
import kotlinmud.attributes.model.startingMv
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.mob.race.impl.Undead
import kotlinmud.mob.type.Gender
import kotlinmud.mob.type.JobType

fun mobBuilder(id: Int, name: String): MobBuilder {
    return MobBuilder()
        .id(id)
        .name(name)
        .hp(startingHp)
        .mana(startingMana)
        .mv(startingMv)
        .race(Human())
        .level(1)
        .gender(if (coinFlip()) Gender.MALE else Gender.FEMALE)
        .attributes(
            AttributesBuilder()
                .hp(startingHp)
                .mana(startingMana)
                .mv(startingMv)
                .build()
        )
}

private fun npc(name: String): MobBuilder {
    return mobBuilder(0, name)
        .isNpc(true)
}

fun zombie(): Mob {
    return npc("a zombie")
        .brief("a zombie is here, ready to attack!")
        .job(JobType.AGGRESSIVE)
        .race(Undead())
        .build()
}

fun deer(): Mob {
    return npc("a deer")
        .brief("a deer weaves through the bushes, trying to avoid attention")
        .description("tbd")
        .race(Deer())
        .build()
}

fun goat(): Mob {
    return npc("a goat")
        .brief("a goat is here, munching on foliage")
        .description("tbd")
        .race(Deer())
        .build()
}

fun turkey(): Mob {
    return npc("a wild turkey")
        .brief("a wild turkey is here, better not get too close")
        .description("tbd")
        .race(Avian())
        .build()
}

fun chicken(): Mob {
    return npc("a chicken")
        .brief("a chicken is here, scratching for worms")
        .race(Avian())
        .build()
}

fun rabbit(): Mob {
    return npc("a rabbit")
        .brief("a rabbit is here, scavenging for its burrow")
        .race(Rabbit())
        .build()
}

fun fox(): Mob {
    return npc("a fox")
        .brief("a fox is here, darting through the brush")
        .race(Canid())
        .build()
}

fun wolf(): Mob {
    return npc("a wolf")
        .brief("a wolf is here, looking for its next meal")
        .job(JobType.AGGRESSIVE)
        .race(Canid())
        .build()
}

fun horse(): Mob {
    return npc("a horse")
        .brief("a wild horse is here, grazing on tall grass")
        .race(Horse())
        .build()
}
