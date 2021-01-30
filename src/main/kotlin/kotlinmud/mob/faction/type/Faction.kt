package kotlinmud.mob.faction.type

import kotlinmud.mob.empire.type.Empire

interface Faction {
    val name: String
    val empire: Empire
}