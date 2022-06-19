package kotlinmud.persistence.token

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.parser.TokenType
import kotlinmud.persistence.parser.Tokenizer

interface Token {
    val token: TokenType
    val terminator: String
    fun parse(builder: Builder, tokenizer: Tokenizer)
}
