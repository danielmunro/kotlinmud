package kotlinmud.mob.race.factory

import kotlinmud.helper.string.matches
import kotlinmud.mob.race.helper.createRaceList
import kotlinmud.mob.race.type.Race

val raceList = createRaceList()

fun createRaceFromString(type: String): Race {
    return raceList.find { it.type.toString() == type } ?: error("that's not a race")
}

fun matchRace(input: String): Race? {
    return raceList.find { input.matches(it.type.toString().toLowerCase()) }
}
