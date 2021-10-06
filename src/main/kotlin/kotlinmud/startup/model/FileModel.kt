package kotlinmud.startup.model

data class FileModel(
    val area: AreaModel,
    val mobs: List<MobModel>,
    val items: List<ItemModel>,
    val rooms: List<RoomModel>,
    val mobRespawns: List<MobRespawnModel>,
    val itemRoomRespawns: List<ItemRoomRespawnModel>,
    val itemMobRespawns: List<ItemMobRespawnModel>,
)
