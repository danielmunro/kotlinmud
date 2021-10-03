package kotlinmud.startup.model

data class MobModel(
    override val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val keywords: Map<String, String>,
) : Model
