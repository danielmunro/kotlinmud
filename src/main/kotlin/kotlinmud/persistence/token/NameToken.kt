package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class NameToken : Token {
    override val token = TokenType.Name
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.name = tokenizer.parseNextToken(this)
    }
}
