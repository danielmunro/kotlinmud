package kotlinmud.fs.loader.area.loader

import kotlinmud.affect.AffectType
import kotlinmud.fs.loader.Tokenizer

abstract class WithAttrLoader : Loader {
    companion object {
        fun parseAffects(tokenizer: Tokenizer): List<AffectType> {
            return tokenizer.parseString().split(",").filter { it != "" }.map {
                AffectType.valueOf(it.trim().toUpperCase())
            }
        }
    }

    var hit = 0
    var dam = 0
    var hp = 0
    var mana = 0
    var mv = 0
    var str = 0
    var int = 0
    var wis = 0
    var dex = 0
    var con = 0
    var acSlash = 0
    var acBash = 0
    var acPierce = 0
    var acMagic = 0

    protected fun parseAffectTypes(tokenizer: Tokenizer): List<AffectType> {
        return tokenizer.parseString().split(",").filter { it != "" }.map {
            AffectType.valueOf(it.trim().toUpperCase())
        }
    }
}
