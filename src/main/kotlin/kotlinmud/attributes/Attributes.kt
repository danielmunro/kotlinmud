package kotlinmud.attributes

const val startingHp = 20
const val startingMana = 100
const val startingMv = 100
const val startingStat = 15
const val startingHit = 1
const val startingDam = 1
const val startingAcBash = 0
const val startingAcSlash = 0
const val startingAcPierce = 0
const val startingAcMagic = 0

data class Attributes(
    val hp: Int = 0,
    val mana: Int = 0,
    val mv: Int = 0,
    val str: Int = 0,
    val int: Int = 0,
    val wis: Int = 0,
    val dex: Int = 0,
    val con: Int = 0,
    val hit: Int = 0,
    val dam: Int = 0,
    val acBash: Int = 0,
    val acSlash: Int = 0,
    val acPierce: Int = 0,
    val acMagic: Int = 0
)
