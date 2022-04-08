package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class DescriptionToken : Token {
    override val token = TokenType.Description
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.description = tokenizer.parseNextToken(this)
    }
}
