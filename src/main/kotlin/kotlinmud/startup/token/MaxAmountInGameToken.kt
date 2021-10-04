package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class MaxAmountInGameToken : Token {
    override val token = TokenType.MaxAmountInGame
    override val terminator = " "
}
