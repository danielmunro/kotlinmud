package kotlinmud.startup.parser

import kotlinmud.helper.logger
import kotlinmud.room.type.Area
import kotlinmud.startup.factory.createNoneAreaModel
import kotlinmud.startup.model.AreaModel
import kotlinmud.startup.model.DoorModel
import kotlinmud.startup.model.FileModel
import kotlinmud.startup.model.ItemMobRespawnModel
import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.ItemRoomRespawnModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.MobRespawnModel
import kotlinmud.startup.model.Model
import kotlinmud.startup.model.QuestModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.parser.exception.TokenParseException
import kotlinmud.startup.spec.AreaSpec
import kotlinmud.startup.spec.DoorSpec
import kotlinmud.startup.spec.ItemSpec
import kotlinmud.startup.spec.MobSpec
import kotlinmud.startup.spec.QuestSpec
import kotlinmud.startup.spec.RoomSpec
import kotlinmud.startup.spec.Spec
import kotlinmud.startup.token.AltMobIdToken
import kotlinmud.startup.token.ItemIdToken
import kotlinmud.startup.token.MaxAmountInGameToken
import kotlinmud.startup.token.MaxAmountInRoomToken
import kotlinmud.startup.token.MobIdToken
import kotlinmud.startup.token.RoomIdToken
import kotlinmud.startup.token.SectionToken
import kotlinmud.startup.validator.FileModelValidator

class ParserService(private val data: String) {
    private var tokenizer = Tokenizer(data)
    private val logger = logger(this)

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
        val doors = mutableListOf<DoorModel>()
        val items = mutableListOf<ItemModel>()
        val quests = mutableListOf<QuestModel>()
        val mobRespawns = mutableListOf<MobRespawnModel>()
        val itemRoomRespawns = mutableListOf<ItemRoomRespawnModel>()
        val itemMobRespawns = mutableListOf<ItemMobRespawnModel>()
        var area = createNoneAreaModel()

        while (tokenizer.isStillReading()) {
            val section = tokenizer.parseNextToken<String>(SectionToken())
            logger.debug("section $section -- area ${area.name}")
            try {
                while (true) {
                    when (section) {
                        "area" -> {
                            area = parseSpec(AreaSpec()) as AreaModel
                        }
                        "rooms" -> rooms.add(parseSpec(RoomSpec()) as RoomModel)
                        "doors" -> doors.add(parseSpec(DoorSpec()) as DoorModel)
                        "items" -> items.add(parseSpec(ItemSpec()) as ItemModel)
                        "mobs" -> mobs.add(parseSpec(MobSpec()) as MobModel)
                        "quests" -> quests.add(parseSpec(QuestSpec()) as QuestModel)
                        "mob_respawns" -> mobRespawns.add(parseMobRespawns(area))
                        "item_room_respawns" -> itemRoomRespawns.add(parseItemRoomRespawns(area))
                        "item_mob_respawns" -> itemMobRespawns.add(parseItemMobRespawns(area))
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
            doors,
            quests,
            mobRespawns,
            itemRoomRespawns,
            itemMobRespawns,
        ).also {
            FileModelValidator(it).validate()
        }
    }

    private fun parseSpec(spec: Spec): Model {
        val builder = spec.builder()
        spec.tokens.forEach { it.parse(builder, tokenizer) }
        return builder.build()
    }

    private fun parseMobRespawns(area: AreaModel): MobRespawnModel {
        val id: Int = tokenizer.parseNextToken(MobIdToken())
        val maxAmountInRoom: Int = tokenizer.parseNextToken(MaxAmountInRoomToken())
        val maxAmountInGame: Int = tokenizer.parseNextToken(MaxAmountInGameToken())
        val roomId: Int = tokenizer.parseNextToken(RoomIdToken())
        return MobRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private fun parseItemRoomRespawns(area: AreaModel): ItemRoomRespawnModel {
        val id: Int = tokenizer.parseNextToken(ItemIdToken())
        val maxAmountInRoom: Int = tokenizer.parseNextToken(MaxAmountInRoomToken())
        val maxAmountInGame: Int = tokenizer.parseNextToken(MaxAmountInGameToken())
        val roomId: Int = tokenizer.parseNextToken(RoomIdToken())
        return ItemRoomRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            roomId,
        )
    }

    private fun parseItemMobRespawns(area: AreaModel): ItemMobRespawnModel {
        val id: Int = tokenizer.parseNextToken(ItemIdToken())
        val maxAmountInRoom: Int = tokenizer.parseNextToken(MaxAmountInRoomToken())
        val maxAmountInGame: Int = tokenizer.parseNextToken(MaxAmountInGameToken())
        val mobId: Int = tokenizer.parseNextToken(AltMobIdToken())
        return ItemMobRespawnModel(
            Area.valueOf(area.name),
            id,
            maxAmountInRoom,
            maxAmountInGame,
            mobId,
        )
    }
}
