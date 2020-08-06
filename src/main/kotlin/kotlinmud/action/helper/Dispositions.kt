package kotlinmud.action.helper

import kotlinmud.mob.type.Disposition

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

fun mustBeResting(): List<Disposition> {
    return listOf(Disposition.SITTING, Disposition.SLEEPING)
}
