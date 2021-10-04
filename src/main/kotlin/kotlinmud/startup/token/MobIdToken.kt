package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class MobIdToken : Token {
    override val token = TokenType.MobId
    override val terminator = " "
}
