package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class IdToken : Token {
    override val token = TokenType.ID
    override val terminator = "."
}
