package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class ItemRespawnToken : Token {
    override val token = TokenType.ItemRespawn
    override val terminator = "~"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {
        builder.respawns = tokenizer.parseItemRespawn()
    }
}
