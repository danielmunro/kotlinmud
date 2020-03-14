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
) {
    class Builder {
        var hp = 0
        var mana = 0
        var mv = 0
        var str = 0
        var int = 0
        var wis = 0
        var dex = 0
        var con = 0
        var hit = 0
        var dam = 0
        var acBash = 0
        var acSlash = 0
        var acPierce = 0
        var acMagic = 0

        fun setHp(value: Int): Builder {
            hp = value
            return this
        }

        fun setMana(value: Int): Builder {
            mana = value
            return this
        }

        fun setMv(value: Int): Builder {
            mv = value
            return this
        }

        fun setStr(value: Int): Builder {
            str = value
            return this
        }

        fun setInt(value: Int): Builder {
            int = value
            return this
        }

        fun setWis(value: Int): Builder {
            wis = value
            return this
        }

        fun setDex(value: Int): Builder {
            dex = value
            return this
        }

        fun setCon(value: Int): Builder {
            con = value
            return this
        }

        fun setHit(value: Int): Builder {
            hit = value
            return this
        }

        fun setDam(value: Int): Builder {
            dam = value
            return this
        }

        fun setAcBash(value: Int): Builder {
            acBash = value
            return this
        }

        fun setAcSlash(value: Int): Builder {
            acSlash = value
            return this
        }

        fun setAcPierce(value: Int): Builder {
            acPierce = value
            return this
        }

        fun setAcMagic(value: Int): Builder {
            acMagic = value
            return this
        }

        fun setAttribute(attribute: Attribute, value: Int) {
            when (attribute) {
                Attribute.HP -> hp = value
                Attribute.MANA -> mana = value
                Attribute.MV -> mv = value
                Attribute.STR -> str = value
                Attribute.INT -> int = value
                Attribute.WIS -> wis = value
                Attribute.DEX -> dex = value
                Attribute.CON -> con = value
                Attribute.HIT -> hit = value
                Attribute.DAM -> dam = value
                Attribute.AC_BASH -> acBash = value
                Attribute.AC_SLASH -> acSlash = value
                Attribute.AC_PIERCE -> acPierce = value
                Attribute.AC_MAGIC -> acMagic = value
            }
        }

        fun build(): Attributes {
            return Attributes(
                hp,
                mana,
                mv,
                str,
                int,
                wis,
                dex,
                con,
                hit,
                dam,
                acBash,
                acSlash,
                acPierce,
                acMagic
            )
        }
    }
}
