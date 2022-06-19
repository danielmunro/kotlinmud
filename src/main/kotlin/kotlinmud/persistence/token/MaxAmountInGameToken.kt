package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class MaxAmountInGameToken : Token {
    override val token = TokenType.MaxAmountInGame
    override val terminator = " "

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
