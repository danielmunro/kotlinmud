package kotlinmud.world.helper

import kotlinmud.data.Table
import kotlinmud.fs.loader.area.mapper.RoomMapper
import kotlinmud.generator.model.World
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

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

fun mapAreasToModel(rooms: List<RoomDAO>, mobs: List<MobDAO>): kotlinmud.world.model.World {
//    val allDoors = rooms.flatMap { it.roomMapper.doors }
//    val mobs = Table(rooms.flatMap { it.mobs }.toMutableList())
//    val allRoomsMapper = RoomMapper(mobs.toList(), allRoomModels, allDoors)
    val items = Table(rooms.flatMap { it.items }.toMutableList())
    val mobResets = Table(rooms.flatMap { it.mobResets }.toMutableList())
    val itemMobResets = Table(rooms.flatMap { it.itemMobResets }.toMutableList())
    val itemRoomResets = Table(rooms.flatMap { it.itemRoomResets }.toMutableList())
    return kotlinmud.world.model.World(
        Table(allRoomsMapper.map().toMutableList()),
        Table(mutableListOf()),
        Table(mobs.toMutableList()),
        items,
        mobResets,
        itemMobResets,
        itemRoomResets
    )
}
