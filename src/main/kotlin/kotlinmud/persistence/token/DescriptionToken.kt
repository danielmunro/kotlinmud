package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class DescriptionToken : Token {
    override val token = TokenType.Description
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.description = tokenizer.parseNextToken(this)
    }
}
