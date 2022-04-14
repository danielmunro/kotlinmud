package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class ItemRespawnToken : Token {
    override val token = TokenType.ItemRespawn
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.respawns = tokenizer.parseItemRespawn()
    }
}
