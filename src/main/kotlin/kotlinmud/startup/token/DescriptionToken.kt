package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class DescriptionToken : Token {
    override val token = TokenType.Description
    override val terminator = "~"
}
