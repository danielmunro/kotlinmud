package kotlinmud.item.helper

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.Material
import kotlinmud.room.dao.RoomDAO
import kotlinmud.type.CanonicalId
import org.jetbrains.exposed.sql.transactions.transaction

class ItemBuilder(private var name: String) {
    private lateinit var description: String
    private lateinit var material: Material
    private var canOwn = true
    private var canonicalId: ItemCanonicalId? = null
    private var room: RoomDAO? = null
    private var food: Food? = null
    private var quantity: Int? = null

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

    fun food(value: Food, quantityValue: Int = 1): ItemBuilder {
        food = value
        quantity = quantityValue
        return this
    }

    fun canonicalId(value: ItemCanonicalId): ItemBuilder {
        canonicalId = value
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
                this.food = this@ItemBuilder.food
                this.quantity = this@ItemBuilder.quantity
                this.canonicalId = this@ItemBuilder.canonicalId
            }
        }
    }
}
