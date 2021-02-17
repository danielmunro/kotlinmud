package kotlinmud.item.helper

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.Material
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class ItemBuilder(private var name: String) {
    private lateinit var description: String
    private lateinit var material: Material
    private var canOwn = false
    private var room: RoomDAO? = null

    fun name(value: String): ItemBuilder {
        name = value
        return this
    }

    fun description(value: String): ItemBuilder {
        description = value
        return this
    }

    fun room(value: RoomDAO): ItemBuilder {
        room = value
        return this
    }

    fun cannotOwn(): ItemBuilder {
        canOwn = false
        return this
    }

    fun material(value: Material): ItemBuilder {
        material = value
        return this
    }

    fun build(): ItemDAO {
        return transaction {
            ItemDAO.new {
                this.name = this@ItemBuilder.name
                this.description = this@ItemBuilder.description
                this.canOwn = this@ItemBuilder.canOwn
                this.material = this@ItemBuilder.material
                this.room = this@ItemBuilder.room
            }
        }
    }
}