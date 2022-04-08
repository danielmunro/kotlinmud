package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class DirectionToken : Token {
    override val token = TokenType.Direction
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
