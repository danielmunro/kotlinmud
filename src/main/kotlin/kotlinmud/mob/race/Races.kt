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
        Reptile()
    )
}
