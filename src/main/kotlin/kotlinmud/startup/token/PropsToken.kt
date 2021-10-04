package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class PropsToken : Token {
    override val token = TokenType.Props
    override val terminator = "~"
}
