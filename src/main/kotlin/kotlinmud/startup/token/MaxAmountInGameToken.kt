package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class MaxAmountInGameToken : Token {
    override val token = TokenType.MaxAmountInGame
    override val terminator = " "

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
