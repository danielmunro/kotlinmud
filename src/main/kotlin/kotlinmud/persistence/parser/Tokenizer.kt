package kotlinmud.persistence.parser

import kotlinmud.persistence.model.builder.RespawnSpec
import kotlinmud.persistence.model.builder.RespawnType
import kotlinmud.persistence.parser.exception.TokenParseException
import kotlinmud.persistence.token.PropsToken
import kotlinmud.persistence.token.SectionToken
import kotlinmud.persistence.token.Token
import java.lang.Exception
import java.lang.NumberFormatException

class Tokenizer(val data: String) {
    var cursor = 0
    var token: Token = SectionToken()

    inline fun <reified T> parseNextToken(nextToken: Token): T {
        token = nextToken
        return parseNextToken()
    }

    inline fun <reified T> parseNextToken(): T {
        var buffer = ""
        var input = ""
        val lastCursor = cursor
        while (input != token.terminator && isStillReading()) {
            buffer += input
            input = data.substring(cursor, cursor + 1)
            cursor += 1
            if (input == "#") {
                input = ""
                cursor = data.indexOf("\n", cursor) + 1
            }
        }
        val trimmed = buffer.trim()
        validateTokenValueType(trimmed, lastCursor)
        return when (T::class) {
            String::class -> trimmed as T
            Int::class -> trimmed.toInt() as T
            else -> throw Exception()
        }
    }

    fun validateTokenValueType(value: String, lastCursor: Int) {
        if (ParserService.isTokenInt(token.token) && !isNumber(value)) {
            cursor = lastCursor
            throw TokenParseException(
                value,
                "Parsed value is not an integer, $token requires int: $value"
            )
        }
    }

    fun parseProps(): Map<String, String> {
        val values = mutableMapOf<String, String>()
        var read = "-1"
        while (read != "") {
            read = parseNextToken(PropsToken())
            if (read != "") {
                val parts = read.split(" ")
                val k = parts[0]
                val v = parts[1]
                values[k] = v
            }
        }
        return values
    }

    fun parseMobRespawn(): List<RespawnSpec> {
        val respawns = mutableListOf<RespawnSpec>()
        var read = "-1"
        while (read != "") {
            read = parseNextToken(PropsToken())
            if (read != "") {
                val parts = read.split(" ")
                respawns.add(
                    RespawnSpec(
                        RespawnType.Mob,
                        parts[0].toInt(),
                        parts[1].toInt(),
                        parts[2].toInt(),
                    )
                )
            }
        }
        return respawns
    }

    fun parseItemRespawn(): List<RespawnSpec> {
        val respawns = mutableListOf<RespawnSpec>()
        var read = "-1"
        while (read != "") {
            read = parseNextToken(PropsToken())
            if (read != "") {
                val parts = read.split(" ")
                respawns.add(
                    RespawnSpec(
                        RespawnType.createFromString(parts[0]),
                        parts[1].toInt(),
                        parts[2].toInt(),
                        parts[3].toInt(),
                    )
                )
            }
        }
        return respawns
    }

    fun isStillReading(): Boolean {
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
