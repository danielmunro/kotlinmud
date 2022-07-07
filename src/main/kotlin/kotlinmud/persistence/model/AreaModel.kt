package kotlinmud.persistence.model

data class AreaModel(
    override val id: Int,
    val name: String,
) : Model {
    val keywords = mapOf<String, String>()
}
