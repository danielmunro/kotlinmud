package kotlinmud.world.service

import kotlinmud.item.builder.ItemBuilder
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.mob.builder.MobBuilder
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.type.JobType
import kotlinmud.mob.type.QuestGiver
import kotlinmud.respawn.helper.itemRespawnsFor
import kotlinmud.room.builder.RoomBuilder
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
    private lateinit var addItemTo: HasInventory

    fun roomBuilder(name: String, description: String): RoomBuilder {
        return roomService.builder(
            name,
            description,
            area,
        ).also {
            lastRoomBuilder = it
            addItemTo = it
        }
    }

    fun buildRoom(): Room {
        return kotlinmud.room.builder.build(lastRoomBuilder).also {
            lastRoom = it
        }
    }

    fun copyRoomBuilder(modifier: (RoomBuilder) -> Unit): RoomBuilder {
        return lastRoomBuilder.copy(modifier).also {
            lastRoomBuilder = it
            addItemTo = it
        }
    }

    fun itemBuilder(name: String, description: String, weight: Double = 1.0): ItemBuilder {
        return itemService.builder(name, description, weight)
    }

    fun build() {
    }

    fun mobBuilder(name: String, brief: String, description: String, race: Race): MobBuilder {
        return mobService.builder(
            name,
            brief,
            description,
            race,
        )
    }

    fun buildShopkeeper(
        name: String,
        brief: String,
        description: String,
        race: Race,
        room: Room,
        items: Map<ItemBuilder, Int>,
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race
        ).also {
            it.room = room
            it.makeShopkeeper()
            itemRespawnsFor(it.canonicalId, items)
            addItemTo = it
        }.build()
    }

    fun buildQuestGiver(
        name: String,
        brief: String,
        description: String,
        race: Race,
        room: Room,
        identifier: QuestGiver
    ) {
        mobService.builder(
            name,
            brief,
            description,
            race,
        ).also {
            it.room = room
            it.job = JobType.QUEST
            it.identifier = identifier
            addItemTo = it
        }.build()
    }
}
