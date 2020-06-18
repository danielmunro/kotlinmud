package kotlinmud.world.helper

import kotlinmud.data.Table
import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.generator.model.World
import kotlinmud.world.model.Area

fun mapGeneratorToModel(world: World): kotlinmud.world.model.World {
    return kotlinmud.world.model.World(
        Table(world.rooms.toMutableList()),
        Table(mutableListOf()),
        Table(world.mobs.toMutableList()),
        Table(mutableListOf()),
        Table(world.mobResets.toMutableList()),
        Table(mutableListOf()),
        Table(mutableListOf())
    )
}

fun mapAreasToModel(areas: List<Area>): kotlinmud.world.model.World {
    val allRoomModels = areas.flatMap { it.roomMapper.roomModels }
    val allDoors = areas.flatMap { it.roomMapper.doors }
    val mobs = Table(areas.flatMap { it.mobs }.toMutableList())
    val allRoomsMapper = RoomMapper(mobs.toList(), allRoomModels, allDoors)
    val doors = Table(allDoors.toMutableList())
    val rooms = Table(allRoomsMapper.map().toMutableList())
    val items = Table(areas.flatMap { it.items }.toMutableList())
    val mobResets = Table(areas.flatMap { it.mobResets }.toMutableList())
    val itemMobResets = Table(areas.flatMap { it.itemMobResets }.toMutableList())
    val itemRoomResets = Table(areas.flatMap { it.itemRoomResets }.toMutableList())
    return kotlinmud.world.model.World(
        rooms,
        doors,
        mobs,
        items,
        mobResets,
        itemMobResets,
        itemRoomResets
    )
}
