package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class AltMobIdToken : Token {
    override val token = TokenType.MobId
    override val terminator = "\n"
}
