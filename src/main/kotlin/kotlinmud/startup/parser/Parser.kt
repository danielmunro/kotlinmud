package kotlinmud.startup.parser

import kotlinmud.room.type.Area
import kotlinmud.startup.model.AreaModel
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.model.Model
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.exception.TokenParseException
import kotlinmud.startup.spec.ItemSpec
import kotlinmud.startup.spec.MobSpec
import kotlinmud.startup.spec.RoomSpec
import kotlinmud.startup.spec.Spec
import kotlinmud.startup.validator.FileModelValidator
import java.lang.Exception
import java.lang.NumberFormatException

class Parser(private val data: String) {
    private var cursor = 0
    private var token = Token.Section
    private var lastRead = ""

    companion object {
        fun isTokenInt(token: Token): Boolean {
            return token == Token.ID ||
                token == Token.MobId ||
                token == Token.RoomId ||
                token == Token.ItemId ||
                token == Token.MaxAmountInRoom ||
                token == Token.MaxAmountInGame
        }
    }

    fun parse(): FileModel {
        val mobs = mutableListOf<MobModel>()
        val mobRespawns = mutableListOf<MobRespawnModel>()
        val itemRoomRespawns = mutableListOf<ItemRoomRespawnModel>()
        val rooms = mutableListOf<RoomModel>()
        val items = mutableListOf<ItemModel>()
        var area = AreaModel(0, "None")
        var section: String
        val buildFile = {
            val file = FileModel(
                area,
                mobs,
                items,
                rooms,
                mobRespawns,
                itemRoomRespawns,
            )
            FileModelValidator(file).validate()
            file
        }
        while (isStillReading()) {
            section = parseNextToken(Token.Section)
            if (section == "") {
                return buildFile()
            }
            try {
                while (true) {
                    when (section) {
                        "area" -> {
                            area = parseArea()
                        }
                        "rooms" -> rooms.add(parseSpec(RoomSpec()) as RoomModel)
                        "items" -> items.add(parseSpec(ItemSpec()) as ItemModel)
                        "mobs" -> mobs.add(parseSpec(MobSpec()) as MobModel)
                        "mob_respawns" -> mobRespawns.add(parseMobRespawns(area))
                        "item_room_respawns" -> itemRoomRespawns.add(parseItemRoomRespawns(area))
                    }
                }
            } catch (e: TokenParseException) {
            }
        }
        return buildFile()
    }

    private fun parseSpec(spec: Spec): Model {
        val builder = spec.builder()
        spec.tokens.forEach {
            when (it) {
                Token.ID -> builder.id = parseNextToken(it)
                Token.Name -> builder.name = parseNextToken(it)
                Token.Brief -> builder.brief = parseNextToken(it)
                Token.Description -> builder.description = parseNextToken(it)
                Token.Props -> builder.keywords = parseProps()
            }
        }
        return builder.build()
    }

    private fun parseArea(): AreaModel {
        val id: Int = parseNextToken(Token.ID)
        val name: String = parseNextToken(Token.Name)
        return AreaModel(id, name)
    }

    private fun parseMobs(): MobModel {
        val id: Int = parseNextToken(Token.ID)
        val name: String = parseNextToken(Token.Name)
        val brief: String = parseNextToken(Token.Brief)
        val description: String = parseNextToken(Token.Description)
        val keywords = parseProps()
        return MobModel(
            id,
            name,
            brief,
            description,
            keywords,
        )
    }

    private fun parseMobRespawns(area: AreaModel): MobRespawnModel {
        val id: Int = parseNextToken(Token.MobId)
        val maxAmountInRoom: Int = parseNextToken(Token.MaxAmountInRoom)
        val maxAmountInGame: Int = parseNextToken(Token.MaxAmountInGame)
        val roomId: Int = parseNextToken(Token.RoomId)
        return MobRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private fun parseItemRoomRespawns(area: AreaModel): ItemRoomRespawnModel {
        val id: Int = parseNextToken(Token.ItemId)
        val maxAmountInRoom: Int = parseNextToken(Token.MaxAmountInRoom)
        val maxAmountInGame: Int = parseNextToken(Token.MaxAmountInGame)
        val roomId: Int = parseNextToken(Token.RoomId)
        return ItemRoomRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private fun parseRoom(): RoomModel {
        val id: Int = parseNextToken(Token.ID)
        val name: String = parseNextToken(Token.Name)
        val description: String = parseNextToken(Token.Description)
        val keywords = parseProps()
        return RoomModel(
            id,
            name,
            description,
            keywords,
        )
    }

    private inline fun <reified T> parseNextToken(nextToken: Token): T {
        token = nextToken
        return parseNextToken()
    }

    private inline fun <reified T> parseNextToken(): T {
        return parseNextToken(
            when (token) {
                Token.Section -> ":"
                Token.ContentType, Token.Name, Token.Brief, Token.RoomId -> "\n"
                Token.ID -> "."
                Token.MobId, Token.ItemId, Token.MaxAmountInGame, Token.MaxAmountInRoom -> " "
                else -> "~"
            }
        )
    }

    private inline fun <reified T> parseNextToken(terminator: String): T {
        var buffer = ""
        lastRead = ""
        val lastCursor = cursor
        while (lastRead != terminator && isStillReading()) {
            buffer += lastRead
            lastRead = data.substring(cursor, cursor + 1)
            cursor += 1
        }
        val trimmed = buffer.trim()
        if (isTokenInt(token) && !isNumber(trimmed)) {
            cursor = lastCursor
            throw TokenParseException(
                trimmed,
                "Parsed value is not an integer, $token requires int: $trimmed"
            )
        }
        return when (T::class) {
            String::class -> trimmed as T
            Int::class -> trimmed.toInt() as T
            else -> throw Exception()
        }
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
