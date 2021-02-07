package kotlinmud.faction.type

import kotlinmud.empire.type.Empire

interface Faction {
    val name: String
    val empire: Empire
}
