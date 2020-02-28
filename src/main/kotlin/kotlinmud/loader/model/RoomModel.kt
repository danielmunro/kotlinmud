package kotlinmud.loader.model

class RoomModel(
    val id: Int,
    val name: String,
    val description: String,
    val north: Int,
    val south: Int,
    val east: Int,
    val west: Int,
    val up: Int,
    val down: Int
) {

    override fun toString(): String {
        return "id: $id\nname: $name\ndescription: $description\nnorth: $north\nsouth: $south\neast: $east\nwest: $west\nup: $up\ndown: $down"
    }
}
