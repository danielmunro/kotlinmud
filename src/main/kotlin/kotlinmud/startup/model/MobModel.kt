package kotlinmud.startup.model

data class MobModel(
    val id: Int,
    val name: String,
    val description: String,
    val keywords: Map<String, String>,
)
