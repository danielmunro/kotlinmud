package kotlinmud.helper

interface Noun : Identifiable {
    override val name: String
    val brief: String
    val description: String
}
