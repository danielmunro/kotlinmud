package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class MaxAmountInRoomToken : Token {
    override val token = TokenType.MaxAmountInRoom
    override val terminator = " "
}
