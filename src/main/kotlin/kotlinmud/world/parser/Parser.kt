package kotlinmud.world.parser

import kotlinmud.world.parser.exception.TokenParseException
import java.io.File
import java.lang.NumberFormatException

class Parser(file: String) {
    private val data = File(file).readText()
    private var section = ""
    private var cursor = 0
    private var token = Token.Section
    private var lastRead = ""

    fun parseFile() {
        while (isStillReading()) {
            println("starting while loop at cursor $cursor")
            section = parseNextToken(Token.Section)
            println("section: '$section'")
            try {
                while (lastRead != "") {
                    when (section) {
                        "rooms" -> parseRoom()
                    }
                }
            } catch (e: TokenParseException) {
                println("token parse catch")
            }
        }
    }

    private fun parseRoom() {
        println("=== parse room === $cursor")
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        println("id: '$id'")
        println("name: '$name'")
        println("description: '$description'")
        var read = "start"
        val directions = mutableMapOf<String, String>()
        while (read != "") {
            read = parseNextToken(Token.Direction)
            if (read != "") {
                val parts = read.split(" ")
                val direction = parts[0]
                val targetRoomId = parts[1]
                println("direction: $direction, targetRoomId: $targetRoomId")
                directions[direction] = targetRoomId
            }
        }
        println("--- done ---")
    }

    private fun peek(): String {
        val currentCursor = cursor
        val token = parseNextToken(Token.ID)
        cursor = currentCursor
        return token
    }

    private fun parseNextToken(nextToken: Token): String {
        token = nextToken
        return parseNextToken()
    }

    private fun parseNextToken(): String {
        println("parseNextToken $token")
        return parseNextToken(
            when (token) {
                Token.Section -> ":"
                Token.ContentType -> "\n"
                Token.ID -> "."
                Token.Name -> "\n"
                Token.Description -> "~"
                Token.Direction -> "~"
            }
        )
    }

    private fun parseNextToken(terminator: String): String {
        var buffer = ""
        lastRead = ""
        val lastCursor = cursor
        while (lastRead != terminator && isStillReading()) {
            buffer += lastRead
            lastRead = data.substring(cursor, cursor + 1)
            cursor += 1
        }
        val trimmed = buffer.trim()
        if (token == Token.ID && !isNumber(trimmed)) {
            cursor = lastCursor
            throw TokenParseException(trimmed, "Parsed value is not an integer, ID requires int: $trimmed")
        }
        return trimmed
    }

    private fun isStillReading(): Boolean {
        return data.length > cursor
    }

    private fun isNumber(trimmed: String): Boolean {
        return try {
            trimmed.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}
