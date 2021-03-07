package kotlinmud.attributes.factory

import kotlinmud.attributes.type.Attribute

fun createStats(str: Int, int: Int, wis: Int, dex: Int, con: Int, hit: Int = 0, dam: Int = 0): MutableMap<Attribute, Int> {
    return mutableMapOf(
        Pair(Attribute.STR, str),
        Pair(Attribute.INT, int),
        Pair(Attribute.WIS, wis),
        Pair(Attribute.DEX, dex),
        Pair(Attribute.CON, con),
        Pair(Attribute.HIT, hit),
        Pair(Attribute.DAM, dam)
    )
}
