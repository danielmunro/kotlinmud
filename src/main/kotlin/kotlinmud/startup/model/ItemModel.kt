package kotlinmud.startup.model

data class ItemModel(
    val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val keywords: Map<String, String>,
)
