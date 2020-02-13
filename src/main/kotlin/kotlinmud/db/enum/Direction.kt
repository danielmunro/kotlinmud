package kotlinmud.db.enum

import kotlinmud.room.Direction
import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

object DirectionTable : Table() {
    val disposition = customEnumeration(
        "direction",
        "DirectionEnum",
        { value -> Direction.valueOf(value as String) },
        { DirectionPGEnum("DirectionEnum", it) })
}

class DirectionPGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name.toString().toLowerCase()
        type = enumTypeName
    }
}
