package kotlinmud.mob.faction.impl

import kotlinmud.mob.empire.impl.Zeta
import kotlinmud.mob.faction.type.Faction

class ZetaInformants : Faction {
    override val name = "Zeta Informants"
    override val empire = Zeta()
}