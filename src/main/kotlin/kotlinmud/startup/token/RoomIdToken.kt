package kotlinmud.startup.token

import kotlinmud.startup.parser.TokenType

class RoomIdToken : Token {
    override val token = TokenType.RoomId
    override val terminator = "\n"
}
