package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class BriefToken : Token {
    override val token = TokenType.Brief
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.brief = tokenizer.parseNextToken(this)
    }
}
