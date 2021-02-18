package kotlinmud.room.helper

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Area
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

class RoomBuilder {
    private lateinit var name: String
    private lateinit var description: String
    private lateinit var area: Area
    private var canonicalId: CanonicalId? = null

    fun name(value: String): RoomBuilder {
        name = value
        return this
    }

    fun description(value: String): RoomBuilder {
        description = value
        return this
    }

    fun area(value: Area): RoomBuilder {
        area = value
        return this
    }

    fun canonicalId(value: CanonicalId?): RoomBuilder {
        canonicalId = value
        return this
    }

    fun build(): RoomDAO {
        val room = transaction {
            RoomDAO.new {
                this.name = this@RoomBuilder.name
                this.description = this@RoomBuilder.description
                this.area = this@RoomBuilder.area
                this.canonicalId = this@RoomBuilder.canonicalId
            }
        }
        canonicalId = null
        return room
    }
}
