package kotlinmud.empire.helper

import kotlinmud.empire.impl.Andalar
import kotlinmud.empire.impl.Highland
import kotlinmud.empire.impl.Zeta
import kotlinmud.empire.type.Empire

fun createEmpireList(): List<Empire> {
    return listOf(
        Andalar(),
        Zeta(),
        Highland(),
    )
}