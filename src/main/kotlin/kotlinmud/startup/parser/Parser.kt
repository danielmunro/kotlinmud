package kotlinmud.startup.parser

import kotlinmud.startup.model.AreaModel
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.exception.TokenParseException
import java.io.File
import java.lang.NumberFormatException

class Parser(file: String) {
    private val data = File(file).readText()
    private var section = ""
    private var cursor = 0
    private var token = Token.Section
    private var lastRead = ""

    fun parseFile(): FileModel {
        val mobs = mutableListOf<MobModel>()
        val rooms = mutableListOf<RoomModel>()
        val items = mutableListOf<ItemModel>()
        var area = AreaModel(0, "placeholder")
        while (isStillReading()) {
            section = parseNextToken(Token.Section)
            if (section == "") {
                return FileModel(
                    area,
                    mobs,
                    items,
                    rooms,
                )
            }
            try {
                while (lastRead != "") {
                    when (section) {
                        "area" -> {
                            area = parseArea()
                        }
                        "rooms" -> rooms.add(parseRoom())
                        "items" -> items.add(parseItem())
                        "mobs" -> mobs.add(parseMobs())
                    }
                }
            } catch (e: TokenParseException) {
            }
        }
        return FileModel(
            area,
            mobs,
            items,
            rooms,
        )
    }

    private fun parseArea(): AreaModel {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        return AreaModel(id.toInt(), name)
    }

    private fun parseMobs(): MobModel {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        val keywords = parseProps()
        return MobModel(
            id.toInt(),
            name,
            description,
            keywords,
        )
    }

    private fun parseItem(): ItemModel {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        val keywords = parseProps()
        return ItemModel(
            id.toInt(),
            name,
            description,
            keywords,
        )
    }

    private fun parseRoom(): RoomModel {
        val id = parseNextToken(Token.ID)
        val name = parseNextToken(Token.Name)
        val description = parseNextToken(Token.Description)
        val keywords = parseProps()
        return RoomModel(
            id.toInt(),
            name,
            description,
            keywords,
        )
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