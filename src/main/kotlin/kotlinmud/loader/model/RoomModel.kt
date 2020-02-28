package kotlinmud.loader.model

class RoomModel(
    val id: Int,
    val name: String,
    val description: String,
    val north: String,
    val south: String,
    val east: String,
    val west: String,
    val up: String,
    val down: String
) {

    override fun toString(): String {
        return "id: $id\nname: $name\ndescription: $description\nnorth: $north\nsouth: $south\neast: $east\nwest: $west\nup: $up\ndown: $down"
    }
}
