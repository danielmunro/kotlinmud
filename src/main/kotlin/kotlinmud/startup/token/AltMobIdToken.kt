package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class AltMobIdToken : Token {
    override val token = TokenType.MobId
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
