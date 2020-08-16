package kotlinmud.mob.race.helper

import kotlinmud.mob.race.impl.Avian
import kotlinmud.mob.race.impl.Bear
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.race.impl.Deer
import kotlinmud.mob.race.impl.Dwarf
import kotlinmud.mob.race.impl.Elf
import kotlinmud.mob.race.impl.Faerie
import kotlinmud.mob.race.impl.Felid
import kotlinmud.mob.race.impl.Giant
import kotlinmud.mob.race.impl.Goat
import kotlinmud.mob.race.impl.Goblin
import kotlinmud.mob.race.impl.Horse
import kotlinmud.mob.race.impl.Human
import kotlinmud.mob.race.impl.Kender
import kotlinmud.mob.race.impl.Lasher
import kotlinmud.mob.race.impl.Lizard
import kotlinmud.mob.race.impl.Ogre
import kotlinmud.mob.race.impl.Rabbit
import kotlinmud.mob.race.impl.Reptile
import kotlinmud.mob.race.impl.Sheep
import kotlinmud.mob.race.impl.Undead
import kotlinmud.mob.race.type.Race

fun createRaceList(): List<Race> {
    return listOf(
        Human(),
        Elf(),
        Kender(),
        Dwarf(),
        Ogre(),
        Giant(),
        Faerie(),
        Lasher(),
        Canid(),
        Felid(),
        Goblin(),
        Reptile(),
        Avian(),
        Deer(),
        Undead(),
        Goat(),
        Rabbit(),
        Horse(),
        Sheep(),
        Bear(),
        Lizard()
    )
}
