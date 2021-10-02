package kotlinmud.startup.parser.exception

class TokenParseException(val buffer: String, override val message: String) : Exception()
