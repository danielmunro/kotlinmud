package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class SectionToken : Token {
    override val token = TokenType.Section
    override val terminator = ":"
}
