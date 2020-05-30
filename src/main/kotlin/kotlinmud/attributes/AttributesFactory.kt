package kotlinmud.attributes

import kotlinmud.attributes.model.Attributes

fun createStats(str: Int, int: Int, wis: Int, dex: Int, con: Int, hit: Int = 0, dam: Int = 0): Attributes {
    return Attributes(
        0,
        0,
        0,
        str,
        int,
        wis,
        dex,
        con,
        hit,
        dam,
        0,
        0,
        0,
        0
    )
}
