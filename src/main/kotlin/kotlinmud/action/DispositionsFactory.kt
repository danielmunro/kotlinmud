package kotlinmud.action

import kotlinmud.mob.Disposition

fun mustBeAlive(): List<Disposition> {
    return listOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING)
}

fun mustBeStanding(): List<Disposition> {
    return listOf(Disposition.STANDING)
}
