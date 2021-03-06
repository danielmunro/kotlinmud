package kotlinmud.app

enum class Environment(val value: String) {
    Development("dev"),
    CI("ci"),
    Test("test"),
    Production("prod");

    companion object {
        fun isDev(): Boolean {
            val env = getDotenv()
            return env["ENV"] == Development.toString() || env["ENV"] == CI.toString()
        }
    }

    override fun toString(): String {
        return value
    }
}
