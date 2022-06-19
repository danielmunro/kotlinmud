package kotlinmud.persistence.model

data class FileModel(
    val area: AreaModel,
    val mobs: List<MobModel>,
    val items: List<ItemModel>,
    val rooms: List<RoomModel>,
    val doors: List<DoorModel>,
    val quests: List<QuestModel>,
    val itemRoomRespawns: List<ItemRoomRespawnModel>,
    val itemMobRespawns: List<ItemMobRespawnModel>,
)
