package kotlinmud.startup.model

data class FileModel(
    val area: AreaModel,
    val mobs: List<MobModel>,
    val items: List<ItemModel>,
    val rooms: List<RoomModel>,
)
