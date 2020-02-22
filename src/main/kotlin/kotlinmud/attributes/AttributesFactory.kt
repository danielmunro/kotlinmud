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

fun createStats(str: Int, int: Int, wis: Int, dex: Int, con: Int, hit: Int = 0, dam: Int = 0): Attributes {
    return Attributes(0, 0, 0, str, int, wis, dex, con, hit, dam, 0, 0, 0, 0)
}
