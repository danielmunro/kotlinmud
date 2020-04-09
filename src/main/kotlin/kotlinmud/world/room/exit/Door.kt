package kotlinmud.world.room.exit

import kotlinmud.data.Row

data class Door(
    override val id: Int,
    val name: String,
    val description: String,
    var disposition: DoorDisposition,
    val keyItemId: Int? = null
) : Row {
    private val defaultDisposition = disposition

    fun reset() {
        disposition = defaultDisposition
    }

    override fun toString(): String {
        return name
    }
}
