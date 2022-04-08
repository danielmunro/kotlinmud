package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class MaxAmountInRoomToken : Token {
    override val token = TokenType.MaxAmountInRoom
    override val terminator = " "

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
