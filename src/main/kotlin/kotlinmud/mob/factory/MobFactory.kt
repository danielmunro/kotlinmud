package kotlinmud.mob.factory

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.race.constant.ZOMBIE_SAVING_THROWS
import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Bear
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Goat
import kotlinmud.mob.race.impl.Horse
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Lizard
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.mob.race.impl.Sheep
import kotlinmud.mob.race.impl.Undead
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.Rarity
import kotlinmud.room.dao.RoomDAO

fun mobBuilder(name: String, room: RoomDAO): MobDAO {
    return MobBuilder()
        .name(name)
        .brief("$name is here")
        .description("$name is here")
        .race(Human())
        .room(room)
        .build()
}

private fun npc(name: String, room: RoomDAO): MobDAO {
    return mobBuilder(name, room).let {
        it.isNpc = true
        it
    }
}

fun zombie(room: RoomDAO): MobDAO {
    return npc("a zombie", room).let {
        it.brief = "a zombie is here, ready to attack!"
        it.job = JobType.AGGRESSIVE
        it.race = Undead()
        it.savingThrows = ZOMBIE_SAVING_THROWS
        it
    }
}

fun skeletonWarrior(room: RoomDAO): MobDAO {
    return npc("a skeleton warrior", room).let {
        it.brief = "a skeleton warrior is here, stalking you!"
        it.job = JobType.AGGRESSIVE
        it.race = Undead()
        it.savingThrows = ZOMBIE_SAVING_THROWS
        it
    }
}

fun deer(room: RoomDAO): MobDAO {
    return npc("a deer", room).let {
        it.brief = "a deer weaves through the bushes, trying to avoid attention"
        it.description = "tbd"
        it.race = Deer()
        it.wimpy = 5
        it
    }
}

fun goat(room: RoomDAO): MobDAO {
    return npc("a goat", room).let {
        it.brief = "a goat is here, munching on foliage"
        it.description = "tbd"
        it.race = Goat()
        it
    }
}

fun sheep(room: RoomDAO): MobDAO {
    return npc("a sheep", room).let {
        it.brief = "a sheep is here, grazing on the grass"
        it.description = "tbd"
        it.race = Sheep()
        it
    }
}

fun turkey(room: RoomDAO): MobDAO {
    return npc("a wild turkey", room).let {
        it.brief = "a wild turkey is here, better not get too close"
        it.description = "tbd"
        it.race = Avian()
        it
    }
}

fun chicken(room: RoomDAO): MobDAO {
    return npc("a chicken", room).let {
        it.brief = "a chicken is here, scratching for worms"
        it.race = Avian()
        it
    }
}

fun rabbit(room: RoomDAO): MobDAO {
    return npc("a rabbit", room).let {
        it.brief = "a rabbit is here, scavenging for its burrow"
        it.race = Rabbit()
        it.wimpy = 5
        it
    }
}

fun fox(room: RoomDAO): MobDAO {
    return npc("a fox", room).let {
        it.brief = "a fox is here, darting through the brush"
        it.race = Canid()
        it
    }
}

fun lizard(room: RoomDAO): MobDAO {
    return npc("a lizard", room).let {
        it.brief = "a lizard is here, sunbathing on a rock"
        it.race = Lizard()
        it.rarity = Rarity.UNCOMMON
        it
    }
}

fun ocelot(room: RoomDAO): MobDAO {
    return npc("an ocelot", room).let {
        it.brief = "an ocelot is here, creeping around"
        it.race = Felid()
        it.rarity = Rarity.RARE
        it
    }
}

fun polarBear(room: RoomDAO): MobDAO {
    return npc("a polar bear", room).let {
        it.brief = "a powerful polar bear is here, better not draw its attention"
        it.race = Bear()
        it.rarity = Rarity.UNCOMMON
        it
    }
}

fun brownBear(room: RoomDAO): MobDAO {
    return npc("a brown bear", room).let {
        it.brief = "a playful brown bear is here, looking for honey"
        it.race = Bear()
        it.rarity = Rarity.UNCOMMON
        it
    }
}

fun blackBear(room: RoomDAO): MobDAO {
    return npc("a black bear", room).let {
        it.brief = "a bear is here, scrounging for food"
        it.race = Bear()
        it.rarity = Rarity.UNCOMMON
        it
    }
}

fun wolf(room: RoomDAO): MobDAO {
    return npc("a wolf", room).let {
        it.brief = "a wolf is here, looking for its next meal"
        it.job = JobType.AGGRESSIVE
        it.race = Canid()
        it.rarity = Rarity.UNCOMMON
        it
    }
}

fun horse(room: RoomDAO): MobDAO {
    return npc("a horse", room).let {
        it.brief = "a wild horse is here, grazing on tall grass"
        it.race = Horse()
        it.rarity = Rarity.UNCOMMON
        it
    }
}
