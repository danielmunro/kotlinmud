package kotlinmud.db.enum

import kotlinmud.mob.Disposition
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object DispositionTable : Table() {
    val disposition = customEnumeration(
        "disposition",
        "DispositionEnum",
        {value -> Disposition.valueOf(value as String)},
        { DispositionPGEnum("DispositionEnum", it)})
}

class DispositionPGEnum<T:Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name.toString().toLowerCase()
        type = enumTypeName
    }
}
