package kotlinmud.mob.race

import kotlinmud.mob.race.impl.*

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
        else -> error("no race: $name")
    }
}
