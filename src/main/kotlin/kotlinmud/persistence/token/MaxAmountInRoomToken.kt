package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class MaxAmountInRoomToken : Token {
    override val token = TokenType.MaxAmountInRoom
    override val terminator = " "

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
