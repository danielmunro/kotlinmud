package kotlinmud.room.exit

class Door(val id: Int, val name: String, val description: String, var disposition: DoorDisposition, val keyItemId: Int? = null) {
    override fun toString(): String {
        return name
    }
}
