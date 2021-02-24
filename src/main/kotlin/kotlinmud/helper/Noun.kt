package kotlinmud.helper

interface Noun : Identifiable {
    override val name: String
    val description: String
}
