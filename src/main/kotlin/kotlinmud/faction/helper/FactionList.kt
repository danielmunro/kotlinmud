package kotlinmud.faction.helper

import kotlinmud.faction.impl.AndalariPraetorians
import kotlinmud.faction.impl.HighlandCouncil
import kotlinmud.faction.impl.ZetaInformants
import kotlinmud.faction.type.Faction

fun factionList(): List<Faction> {
    return listOf(
        AndalariPraetorians(),
        HighlandCouncil(),
        ZetaInformants()
    )
}
