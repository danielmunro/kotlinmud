package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class ContentTypeToken : Token {
    override val token = TokenType.ContentType
    override val terminator = "\n"
}
