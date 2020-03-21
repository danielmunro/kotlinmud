package kotlinmud.loader.loader

abstract class WithAttrLoader : Loader {
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

    fun parseAttributes() {
        hit = intAttr("hit")
        dam = intAttr("dam")
        hp = intAttr("hp")
        mana = intAttr("mana")
        mv = intAttr("mv")
        str = intAttr("str")
        int = intAttr("int")
        wis = intAttr("wis")
        dex = intAttr("dex")
        con = intAttr("con")
        val ac = strAttr("ac", "0-0-0-0").split("-")
        acSlash = ac[0].toInt()
        acBash = ac[1].toInt()
        acPierce = ac[2].toInt()
        acMagic = ac[3].toInt()
    }
}
