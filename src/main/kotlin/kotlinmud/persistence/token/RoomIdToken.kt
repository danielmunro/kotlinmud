package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

class RoomIdToken : Token {
    override val token = TokenType.RoomId
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
