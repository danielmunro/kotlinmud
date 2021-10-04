package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

interface Token {
    val token: TokenType
    val terminator: String
}
