package kotlinmud.startup.model

data class RoomModel(
    val id: Int,
    val name: String,
    val description: String,
    val keywords: Map<String, String>,
)
