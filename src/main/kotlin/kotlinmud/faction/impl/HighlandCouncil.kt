package kotlinmud.faction.impl

import kotlinmud.empire.impl.Highland
import kotlinmud.faction.type.Faction
import kotlinmud.faction.type.FactionType

class HighlandCouncil : Faction {
    override val name = "Highland Council"
    override val type = FactionType.HIGHLAND_COUNCIL
    override val empire = Highland()
}
