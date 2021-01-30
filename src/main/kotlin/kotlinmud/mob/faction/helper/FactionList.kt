package kotlinmud.mob.faction.helper

import kotlinmud.mob.faction.impl.AndalariPraetorians
import kotlinmud.mob.faction.impl.HighlandCouncil
import kotlinmud.mob.faction.impl.ZetaInformants
import kotlinmud.mob.faction.type.Faction

fun factionList(): List<Faction> {
    return listOf(
        AndalariPraetorians(),
        HighlandCouncil(),
        ZetaInformants()
    )
}
