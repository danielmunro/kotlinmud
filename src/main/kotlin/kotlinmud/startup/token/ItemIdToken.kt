package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class ItemIdToken : Token {
    override val token = TokenType.ItemId
    override val terminator = " "
}
