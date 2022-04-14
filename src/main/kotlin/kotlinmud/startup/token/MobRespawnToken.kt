package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class MobRespawnToken : Token {
    override val token = TokenType.MobRespawn
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.respawns = tokenizer.parseMobRespawn()
    }
}
