package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class BriefToken : Token {
    override val token = TokenType.Brief
    override val terminator = "\n"
}
