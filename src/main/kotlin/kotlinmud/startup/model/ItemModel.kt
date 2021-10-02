package kotlinmud.startup.model

data class ItemModel(
    val id: Int,
    val name: String,
    val description: String,
    val keywords: Map<String, String>,
)
