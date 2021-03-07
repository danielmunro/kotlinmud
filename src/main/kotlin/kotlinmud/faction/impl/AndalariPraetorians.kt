package kotlinmud.faction.impl

import kotlinmud.empire.impl.Andalar
import kotlinmud.faction.type.Faction
import kotlinmud.faction.type.FactionType

class AndalariPraetorians : Faction {
    override val name = "Andalari Praetorians"
    override val type = FactionType.PRAETORIAN_GUARD
    override val empire = Andalar()
}
