package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class NameToken : Token {
    override val token = TokenType.Name
    override val terminator = "\n"
}
