package kotlinmud.mob.race.factory

import Horse
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
import kotlinmud.mob.race.type.RaceType

fun createRaceFromType(name: String): Race {
    return when (name) {
        RaceType.HUMAN.toString() -> Human()
        RaceType.ELF.toString() -> Elf()
        RaceType.KENDER.toString() -> Kender()
        RaceType.DWARF.toString() -> Dwarf()
        RaceType.OGRE.toString() -> Ogre()
        RaceType.GIANT.toString() -> Giant()
        RaceType.FAERIE.toString() -> Faerie()
        RaceType.LASHER.toString() -> Lasher()
        RaceType.CANID.toString() -> Canid()
        RaceType.FELID.toString() -> Felid()
        RaceType.GOBLIN.toString() -> Goblin()
        RaceType.REPTILE.toString() -> Reptile()
        RaceType.AVIAN.toString() -> Avian()
        RaceType.DEER.toString() -> Deer()
        RaceType.UNDEAD.toString() -> Undead()
        RaceType.GOAT.toString() -> Goat()
        RaceType.RABBIT.toString() -> Rabbit()
        RaceType.HORSE.toString() -> Horse()
        RaceType.SHEEP.toString() -> Sheep()
        RaceType.BEAR.toString() -> Bear()
        RaceType.LIZARD.toString() -> Lizard()
        else -> error("no race: $name")
    }
}
