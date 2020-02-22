package kotlinmud.attributes

import kotlinmud.mob.Race

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

fun createRaceAttributes(race: Race): Attributes {
    return when (race) {
        Race.HUMAN -> Attributes()
        Race.ELF -> createStats(-2, 1, 2, 1, -2)
        Race.KENDER -> createStats(-1, 0, 1, 2, 1, 1, 0)
        Race.DWARF -> createStats(2, -2, 0, -1, 1, 0, 1)
        Race.OGRE -> createStats(2, -2, -2, 0, 2, 0, 1)
        Race.GIANT -> createStats(3, -2, -2, -2, 3, 0, 1)
        Race.FAERIE -> createStats(-3, 2, 2, 2, -3, 1, -1)
        Race.LASHER -> createStats(1, -1, -2, 1, 1, 1, 0)
    }
}

fun createStats(str: Int, int: Int, wis: Int, dex: Int, con: Int, hit: Int = 0, dam: Int = 0): Attributes {
    return Attributes(0, 0, 0, str, int, wis, dex, con, 0, 0, 0, 0, 0, 0)
}
