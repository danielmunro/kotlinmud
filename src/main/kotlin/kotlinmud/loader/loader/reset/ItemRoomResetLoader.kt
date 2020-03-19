package kotlinmud.loader.loader.reset

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.loader.Loader
import kotlinmud.loader.model.Model
import kotlinmud.loader.model.reset.ItemRoomReset

class ItemRoomResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var itemId = 0
    var roomId = 0
    var maxInRoom = 0
    var maxInWorld = 0

    override var props: Map<String, String>
        get() = TODO("Not yet implemented")
        set(value) {}

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