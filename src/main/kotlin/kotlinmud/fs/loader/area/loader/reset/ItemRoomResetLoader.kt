package kotlinmud.fs.loader.area.loader.reset

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.model.Model
import kotlinmud.fs.loader.area.model.reset.ItemRoomReset

class ItemRoomResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var itemId = 0
    var roomId = 0
    var maxInRoom = 0
    var maxInWorld = 0

    override var props: Map<String, String>
        get() = TODO("Not yet implemented")
        set(_) {}

    override fun load(): Model {
        id++
        val props = tokenizer.parseProperties()
        itemId = props["itemId"]!!.toInt()
        roomId = props["roomId"]!!.toInt()
        maxInRoom = props["maxInRoom"]?.toInt() ?: 1
        maxInWorld = props["maxInWorld"]?.toInt() ?: 1

        return ItemRoomReset(id, itemId, roomId, maxInRoom, maxInWorld)
    }
}
