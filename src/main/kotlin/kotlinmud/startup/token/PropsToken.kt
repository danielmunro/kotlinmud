package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class PropsToken : Token {
    override val token = TokenType.Props
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.keywords = tokenizer.parseProps()
    }
}
