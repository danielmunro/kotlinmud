package kotlinmud.startup.token

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.TokenType
import kotlinmud.startup.parser.Tokenizer

interface Token {
    val token: TokenType
    val terminator: String
    fun parse(builder: Builder, tokenizer: Tokenizer)
}
