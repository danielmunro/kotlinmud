package kotlinmud.room.exit

data class Door(
    val id: Int,
    val name: String,
    val description: String,
    var disposition: DoorDisposition,
    val keyItemId: Int? = null
) {
    private val defaultDisposition = disposition

    fun reset() {
        disposition = defaultDisposition
    }

    override fun toString(): String {
        return name
    }
}
