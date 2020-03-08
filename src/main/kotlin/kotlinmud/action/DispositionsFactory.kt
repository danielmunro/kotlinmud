package kotlinmud.action

import kotlinmud.mob.Disposition

fun mustBeAlive(): List<Disposition> {
    return listOf(
        Disposition.STANDING,
        Disposition.SITTING,
        Disposition.FIGHTING,
        Disposition.SLEEPING
    )
}

fun mustBeAwake(): List<Disposition> {
    return listOf(Disposition.SITTING, Disposition.STANDING, Disposition.FIGHTING)
}

fun mustBeStanding(): List<Disposition> {
    return listOf(Disposition.STANDING)
}

fun mustBeAlert(): List<Disposition> {
    return listOf(Disposition.STANDING, Disposition.FIGHTING)
}

fun mustBeFighting(): List<Disposition> {
    return listOf(Disposition.FIGHTING)
}
