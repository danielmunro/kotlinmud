package kotlinmud.startup.parser

import kotlinmud.room.type.Area
import kotlinmud.startup.factory.createNoneAreaModel
import kotlinmud.startup.model.AreaModel
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.model.Model
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.exception.TokenParseException
import kotlinmud.startup.spec.AreaSpec
import kotlinmud.startup.spec.ItemSpec
import kotlinmud.startup.spec.MobSpec
import kotlinmud.startup.spec.RoomSpec
import kotlinmud.startup.spec.Spec
import kotlinmud.startup.token.BriefToken
import kotlinmud.startup.token.DescriptionToken
import kotlinmud.startup.token.DirectionToken
import kotlinmud.startup.token.IdToken
import kotlinmud.startup.token.ItemIdToken
import kotlinmud.startup.token.MaxAmountInGameToken
import kotlinmud.startup.token.MaxAmountInRoomToken
import kotlinmud.startup.token.MobIdToken
import kotlinmud.startup.token.NameToken
import kotlinmud.startup.token.PropsToken
import kotlinmud.startup.token.RoomIdToken
import kotlinmud.startup.token.SectionToken
import kotlinmud.startup.token.Token
import kotlinmud.startup.validator.FileModelValidator
import java.lang.Exception
import java.lang.NumberFormatException

class Parser(private val data: String) {
    private var cursor = 0
    private var token: Token = SectionToken()

    companion object {
        fun isTokenInt(token: TokenType): Boolean {
            return token == TokenType.ID ||
                token == TokenType.MobId ||
                token == TokenType.RoomId ||
                token == TokenType.ItemId ||
                token == TokenType.MaxAmountInRoom ||
                token == TokenType.MaxAmountInGame
        }
    }

    fun parse(): FileModel {
        val mobs = mutableListOf<MobModel>()
        val rooms = mutableListOf<RoomModel>()
        val items = mutableListOf<ItemModel>()
        val mobRespawns = mutableListOf<MobRespawnModel>()
        val itemRoomRespawns = mutableListOf<ItemRoomRespawnModel>()
        var area = createNoneAreaModel()

        while (isStillReading()) {
            val section = parseNextToken<String>(SectionToken())
            try {
                while (true) {
                    when (section) {
                        "area" -> {
                            area = parseSpec(AreaSpec()) as AreaModel
                        }
                        "rooms" -> rooms.add(parseSpec(RoomSpec()) as RoomModel)
                        "items" -> items.add(parseSpec(ItemSpec()) as ItemModel)
                        "mobs" -> mobs.add(parseSpec(MobSpec()) as MobModel)
                        "mob_respawns" -> mobRespawns.add(parseMobRespawns(area))
                        "item_room_respawns" -> itemRoomRespawns.add(parseItemRoomRespawns(area))
                        "" -> break
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
            mobRespawns,
            itemRoomRespawns,
        ).also {
            FileModelValidator(it).validate()
        }
    }

    private fun parseSpec(spec: Spec): Model {
        val builder = spec.builder()
        spec.tokens.forEach {
            when (it) {
                is IdToken -> builder.id = parseNextToken(it)
                is NameToken -> builder.name = parseNextToken(it)
                is BriefToken -> builder.brief = parseNextToken(it)
                is DescriptionToken -> builder.description = parseNextToken(it)
                is PropsToken -> builder.keywords = parseProps()
                else -> throw Exception()
            }
        }
        return builder.build()
    }

    private fun parseMobRespawns(area: AreaModel): MobRespawnModel {
        val id: Int = parseNextToken(MobIdToken())
        val maxAmountInRoom: Int = parseNextToken(MaxAmountInRoomToken())
        val maxAmountInGame: Int = parseNextToken(MaxAmountInGameToken())
        val roomId: Int = parseNextToken(RoomIdToken())
        return MobRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private fun parseItemRoomRespawns(area: AreaModel): ItemRoomRespawnModel {
        val id: Int = parseNextToken(ItemIdToken())
        val maxAmountInRoom: Int = parseNextToken(MaxAmountInRoomToken())
        val maxAmountInGame: Int = parseNextToken(MaxAmountInGameToken())
        val roomId: Int = parseNextToken(RoomIdToken())
        return ItemRoomRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private inline fun <reified T> parseNextToken(nextToken: Token): T {
        token = nextToken
        return parseNextToken()
    }

    private inline fun <reified T> parseNextToken(): T {
        var buffer = ""
        var input = ""
        val lastCursor = cursor
        while (input != token.terminator && isStillReading()) {
            buffer += input
            input = data.substring(cursor, cursor + 1)
            cursor += 1
        }
        val trimmed = buffer.trim()
        if (isTokenInt(token.token) && !isNumber(trimmed)) {
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
            read = parseNextToken(DirectionToken())
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
