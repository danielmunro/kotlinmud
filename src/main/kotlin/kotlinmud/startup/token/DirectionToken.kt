package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class DirectionToken : Token {
    override val token = TokenType.Direction
    override val terminator = "~"
}
