package kotlinmud.item

class Inventory(
    val items: MutableList<Item> = mutableListOf(),
    val maxItems: Int = 0,
    val maxWeight: Int = 0
) {
    fun copy(): Inventory {
        return Inventory(mutableListOf(), maxItems, maxWeight)
    }
}
