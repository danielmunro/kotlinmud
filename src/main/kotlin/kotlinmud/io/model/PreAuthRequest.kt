package kotlinmud.io.model

class PreAuthRequest(val client: Client, val input: String) {
    private val args = input.split(" ")

    fun arg(positionalIndex: Int): String? {
        return args.getOrNull(positionalIndex)
    }
}
