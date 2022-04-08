package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class NameToken : Token {
    override val token = TokenType.Name
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.name = tokenizer.parseNextToken(this)
    }
}
