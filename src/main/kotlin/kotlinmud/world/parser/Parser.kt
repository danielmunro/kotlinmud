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
            section = parseNextToken(Token.Section)
            println("section: '$section'")
            if (section == "") {
                return
            }
            try {
                while (lastRead != "") {
                    when (section) {
                        "rooms" -> parseRoom()
                        "items" -> parseItem()
                        "mobs" -> parseMobs()
                    }
                }
            } catch (e: TokenParseException) {
            }
        }
    }

    private fun parseMobs() {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        val attributes = parseProps()
        val affects = parseProps()
        println("mob: $id - $name - $description")
    }

    private fun parseItem() {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        val keyword = parseNextToken(Token.Keyword)
        val attributes = parseProps()
        val affects = parseProps()
        println("item: $id - $name - $description - $keyword")
    }

    private fun parseRoom() {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        println("id: '$id'")
        println("name: '$name'")
        println("description: '$description'")
        val directions = parseProps()
        directions.forEach {
            println("directions: ${it.key} - ${it.value}")
        }
    }

    private fun parseNextToken(nextToken: Token): String {
        token = nextToken
        return parseNextToken()
    }

    private fun parseNextToken(): String {
        return parseNextToken(
            when (token) {
                Token.Section -> ":"
                Token.ContentType, Token.Name -> "\n"
                Token.ID -> "."
                else -> "~"
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

    private fun parseProps(): Map<String, String> {
        val values = mutableMapOf<String, String>()
        var read = "-1"
        while (read != "") {
            read = parseNextToken(Token.Direction)
            if (read != "") {
                val parts = read.split(" ")
                val k = parts[0]
                val v = parts[1]
                values[k] = v
            }
        }
        return values
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
