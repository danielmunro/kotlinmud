package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class PropsToken : Token {
    override val token = TokenType.Props
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.keywords = tokenizer.parseProps()
    }
}
