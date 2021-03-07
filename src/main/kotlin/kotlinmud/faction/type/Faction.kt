package kotlinmud.faction.type

import kotlinmud.empire.type.Empire

interface Faction {
    val name: String
    val type: FactionType
    val empire: Empire
}
