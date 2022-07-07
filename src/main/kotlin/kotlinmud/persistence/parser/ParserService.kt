package kotlinmud.persistence.parser

import kotlinmud.helper.logger
import kotlinmud.persistence.model.AreaModel
import kotlinmud.persistence.model.DoorModel
import kotlinmud.persistence.model.FileModel
import kotlinmud.persistence.model.ItemMobRespawnModel
import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.model.ItemRoomRespawnModel
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.Model
import kotlinmud.persistence.model.QuestModel
import kotlinmud.persistence.model.RoomModel
import kotlinmud.persistence.parser.exception.TokenParseException
import kotlinmud.persistence.spec.AreaSpec
import kotlinmud.persistence.spec.DoorSpec
import kotlinmud.persistence.spec.ItemSpec
import kotlinmud.persistence.spec.MobSpec
import kotlinmud.persistence.spec.QuestSpec
import kotlinmud.persistence.spec.RoomSpec
import kotlinmud.persistence.spec.Spec
import kotlinmud.persistence.token.SectionToken
import kotlinmud.persistence.validator.FileModelValidator
import kotlinmud.room.factory.createPurgatoryArea
import kotlinmud.room.model.Area
import kotlinmud.room.type.Lighting

class ParserService(data: String) {
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
        val itemRoomRespawns = mutableListOf<ItemRoomRespawnModel>()
        val itemMobRespawns = mutableListOf<ItemMobRespawnModel>()
        var area = createPurgatoryArea()

        while (tokenizer.isStillReading()) {
            val section = tokenizer.parseNextToken<String>(SectionToken())
            logger.debug("section $section -- area ${area.name}")
            try {
                while (true) {
                    when (section) {
                        "area" -> {
                            val areaModel = parseSpec(AreaSpec()) as AreaModel
                            area = Area(
                                areaModel.id,
                                areaModel.name,
                                Lighting.valueOf(areaModel.keywords.getOrDefault("lighting", "Good")),
                            )
                        }
                        "rooms" -> rooms.add(parseSpec(RoomSpec()) as RoomModel)
                        "doors" -> doors.add(parseSpec(DoorSpec()) as DoorModel)
                        "items" -> items.add(parseSpec(ItemSpec(area)) as ItemModel)
                        "mobs" -> mobs.add(parseSpec(MobSpec(area)) as MobModel)
                        "quests" -> quests.add(parseSpec(QuestSpec()) as QuestModel)
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
}
