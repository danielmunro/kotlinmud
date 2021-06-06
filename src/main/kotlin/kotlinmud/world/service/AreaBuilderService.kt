package kotlinmud.world.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.builder.build
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area

class AreaBuilderService(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val itemService: ItemService,
    private val area: Area,
) {

    private lateinit var lastRoomBuilder: RoomBuilder
    private lateinit var lastRoom: Room

    fun roomBuilder(name: String, description: String): RoomBuilder {
        return roomService.builder(
            name,
            description,
            area,
        ).also {
            lastRoomBuilder = it
        }
    }

    fun buildRoomCopy(modifier: (RoomBuilder) -> Unit): Room {
        return build(
            lastRoomBuilder.copy(modifier).also {
                lastRoomBuilder = it
            }
        ).also {
            lastRoom = it
        }
    }

    fun itemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemService.builder(name, description, weight)
    }

    fun buildShopkeeper(
        name: String,
        brief: String,
        description: String,
        race: Race,
        items: Map<ItemBuilder, Int>,
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race
        ).also {
            it.room = lastRoom
            it.makeShopkeeper()
            itemRespawnsFor(it.canonicalId, items)
        }.build()
    }

    fun buildQuestGiver(
        name: String,
        brief: String,
        description: String,
        race: Race,
        identifier: QuestGiver
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race,
        ).also {
            it.room = lastRoom
            it.job = JobType.QUEST
            it.identifier = identifier
        }.build()
    }
}
