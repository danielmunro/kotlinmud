package kotlinmud.mob

enum class Size {
    XSMALL,
    SMALL,
    MEDIUM,
    LARGE,
    XLARGE,
}

fun getSizeForRace(race: Race): Size {
    return when (race) {
        Race.GIANT -> Size.XLARGE
        Race.FAERIE -> Size.XSMALL
        Race.OGRE -> Size.LARGE
        Race.DWARF -> Size.SMALL
        Race.ELF -> Size.SMALL
        Race.HUMAN -> Size.MEDIUM
        Race.KENDER -> Size.SMALL
        Race.LASHER -> Size.LARGE
    }
}
