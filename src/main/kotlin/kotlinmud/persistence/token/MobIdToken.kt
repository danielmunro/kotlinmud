package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class MobIdToken : Token {
    override val token = TokenType.MobId
    override val terminator = " "

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
