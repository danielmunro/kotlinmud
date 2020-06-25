package kotlinmud.mob.factory

import Horse
import kotlinmud.attributes.model.AttributesBuilder
import kotlinmud.attributes.model.startingHp
import kotlinmud.attributes.model.startingMana
import kotlinmud.attributes.model.startingMv
import kotlinmud.helper.math.coinFlip
import kotlinmud.mob.model.MobBuilder
import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Bear
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Goat
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.mob.race.impl.Sheep
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

fun zombie(): MobBuilder {
    return npc("a zombie")
        .brief("a zombie is here, ready to attack!")
        .job(JobType.AGGRESSIVE)
        .race(Undead())
}

fun skeletonWarrior(): MobBuilder {
    return npc("a skeleton warrior")
        .brief("a skeleton warrior is here, stalking you!")
        .job(JobType.AGGRESSIVE)
        .race(Undead())
}

fun deer(): MobBuilder {
    return npc("a deer")
        .brief("a deer weaves through the bushes, trying to avoid attention")
        .description("tbd")
        .race(Deer())
}

fun goat(): MobBuilder {
    return npc("a goat")
        .brief("a goat is here, munching on foliage")
        .description("tbd")
        .race(Goat())
}

fun sheep(): MobBuilder {
    return npc("a sheep")
        .brief("a sheep is here, grazing on the grass")
        .description("tbd")
        .race(Sheep())
}

fun turkey(): MobBuilder {
    return npc("a wild turkey")
        .brief("a wild turkey is here, better not get too close")
        .description("tbd")
        .race(Avian())
}

fun chicken(): MobBuilder {
    return npc("a chicken")
        .brief("a chicken is here, scratching for worms")
        .race(Avian())
}

fun rabbit(): MobBuilder {
    return npc("a rabbit")
        .brief("a rabbit is here, scavenging for its burrow")
        .race(Rabbit())
}

fun fox(): MobBuilder {
    return npc("a fox")
        .brief("a fox is here, darting through the brush")
        .race(Canid())
}

fun lizard(): MobBuilder {
    return npc("a lizard")
        .brief("a lizard is here, sunbathing on a rock")
        .race(Canid())
}

fun ocelot(): MobBuilder {
    return npc("an ocelot")
        .brief("an ocelot is here, creeping around")
        .race(Canid())
}

fun polarBear(): MobBuilder {
    return npc("a polar bear")
        .brief("a powerful polar bear is here, better not draw its attention")
        .race(Bear())
}

fun brownBear(): MobBuilder {
    return npc("a brown bear")
        .brief("a playful brown bear is here, looking for honey")
        .race(Bear())
}

fun blackBear(): MobBuilder {
    return npc("a black bear")
        .brief("a bear is here, scrounging for food")
        .race(Bear())
}

fun wolf(): MobBuilder {
    return npc("a wolf")
        .brief("a wolf is here, looking for its next meal")
        .job(JobType.AGGRESSIVE)
        .race(Canid())
}

fun horse(): MobBuilder {
    return npc("a horse")
        .brief("a wild horse is here, grazing on tall grass")
        .race(Horse())
}
