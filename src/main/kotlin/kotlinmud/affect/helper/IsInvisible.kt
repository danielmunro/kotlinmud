package kotlinmud.affect.helper

import kotlinmud.affect.type.AffectType
import kotlinmud.helper.Noun
import org.jetbrains.exposed.sql.transactions.transaction

fun isInvisible(noun: Noun): Boolean {
    return transaction { noun.affects.find { it.type == AffectType.INVISIBILITY } } != null
}
