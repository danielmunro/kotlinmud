package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

class RoomIdToken : Token {
    override val token = TokenType.RoomId
    override val terminator = "\n"

    override fun parse(builder: Builder, tokenizer: Tokenizer) {}
}
