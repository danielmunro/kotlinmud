package kotlinmud.mob.empire.helper

import kotlinmud.mob.empire.impl.Andalar
import kotlinmud.mob.empire.impl.Highland
import kotlinmud.mob.empire.impl.Zeta
import kotlinmud.mob.empire.type.Empire

fun createEmpireList(): List<Empire> {
    return listOf(
        Andalar(),
        Zeta(),
        Highland(),
    )
}