package kotlinmud.mob

import kotlinmud.mob.race.RaceType

enum class Size {
    TINY,
    SMALL,
    MEDIUM,
    LARGE,
    HUGE,
}

fun getSizeForRace(race: RaceType): Size {
    return when (race) {
        RaceType.GIANT -> Size.HUGE
        RaceType.FAERIE -> Size.TINY
        RaceType.OGRE -> Size.LARGE
        RaceType.DWARF -> Size.SMALL
        RaceType.ELF -> Size.SMALL
        RaceType.HUMAN -> Size.MEDIUM
        RaceType.KENDER -> Size.SMALL
        RaceType.LASHER -> Size.LARGE
    }
}
