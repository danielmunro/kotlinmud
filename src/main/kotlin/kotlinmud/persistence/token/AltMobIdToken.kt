package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class AltMobIdToken : Token {
    override val token = TokenType.MobId
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
