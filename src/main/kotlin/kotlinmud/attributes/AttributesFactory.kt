package kotlinmud.attributes

fun createDefaultMobAttributes(): Attributes {
    return Attributes(
        startingHp,
        startingMana,
        startingMv,
        startingStat,
        startingStat,
        startingStat,
        startingStat,
        startingStat,
        startingHit,
        startingDam,
        startingAcBash,
        startingAcSlash,
        startingAcPierce,
        startingAcMagic)
}
