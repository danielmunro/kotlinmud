package kotlinmud.item

interface Recipe {
    val name: String
    fun getComponents(): Map<ItemType, Int>
    fun getProducts(): List<Item>
}
