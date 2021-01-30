package kotlinmud.mob.faction.impl

import kotlinmud.mob.empire.impl.Highland
import kotlinmud.mob.faction.type.Faction

class HighlandCouncil : Faction {
    override val name = "Highland Council"
    override val empire = Highland()
}