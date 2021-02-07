package kotlinmud.faction.impl

import kotlinmud.empire.impl.Highland
import kotlinmud.faction.type.Faction

class HighlandCouncil : Faction {
    override val name = "Highland Council"
    override val empire = Highland()
}
