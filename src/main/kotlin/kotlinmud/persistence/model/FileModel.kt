package kotlinmud.persistence.model

import kotlinmud.room.model.Area

data class FileModel(
    val area: Area,
    val mobs: List<MobModel>,
    val items: List<ItemModel>,
    val rooms: List<RoomModel>,
    val doors: List<DoorModel>,
    val quests: List<QuestModel>,
    val itemRoomRespawns: List<ItemRoomRespawnModel>,
    val itemMobRespawns: List<ItemMobRespawnModel>,
)
