package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class IdToken : Token {
    override val token = TokenType.ID
    override val terminator = "."

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.id = tokenizer.parseNextToken(this)
    }
}
