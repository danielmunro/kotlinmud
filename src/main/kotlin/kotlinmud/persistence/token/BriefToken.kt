package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class BriefToken : Token {
    override val token = TokenType.Brief
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.brief = tokenizer.parseNextToken(this)
    }
}
