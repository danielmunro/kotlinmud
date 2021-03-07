package kotlinmud.mob.race.factory

import kotlinmud.helper.string.matches
import kotlinmud.mob.race.helper.createPlayableRaceList
import kotlinmud.mob.race.helper.createRaceList
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.race.type.RaceType

val raceList = createRaceList()
val playableRaceList = createPlayableRaceList()

fun createRaceFromString(type: RaceType): Race {
    return raceList.find { it.type == type } ?: error("that's not a race")
}

fun matchPlayableRace(input: String): Race? {
    return playableRaceList.find { input.matches(it.type.toString().toLowerCase()) }
}
