package kotlinmud.mob.race

import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Kender
import kotlinmud.mob.race.impl.Lasher
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.race.impl.Reptile

fun createRaceFromString(name: String): Race {
    return when (name) {
        "human" -> Human()
        "elf" -> Elf()
        "kender" -> Kender()
        "dwarf" -> Dwarf()
        "ogre" -> Ogre()
        "giant" -> Giant()
        "faerie" -> Faerie()
        "lasher" -> Lasher()
        "canid" -> Canid()
        "felid" -> Felid()
        "goblin" -> Goblin()
        "reptile" -> Reptile()
        else -> error("no race: $name")
    }
}
