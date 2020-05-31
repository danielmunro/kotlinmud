package kotlinmud.fs.loader.area.loader

import kotlinmud.affect.type.AffectType
import kotlinmud.fs.loader.Tokenizer

interface Loader {
    fun load(): Any
}

fun parseAffects(tokenizer: Tokenizer): List<AffectType> {
    return tokenizer.parseString().split(",").filter { it != "" }.map {
        AffectType.valueOf(it.trim().toUpperCase())
    }
}

fun intAttr(value: String?, default: Int = 0): Int {
    return value?.toInt() ?: default
}

fun strAttr(value: String?, default: String = ""): String {
    return value ?: default
}
