package kotlinmud.loader.loader

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.RoomModel

class RoomLoader(private val tokenizer: Tokenizer) {
    var id = 0
    var name = ""
    var description = ""
    var north = 0
    var south = 0
    var east = 0
    var west = 0
    var up = 0
    var down = 0

    fun load(): RoomModel {
        id = tokenizer.parseId()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        val props = tokenizer.parseProperties()
        north = props["n"]?.toInt() ?: 0
        south = props["s"]?.toInt() ?: 0
        east = props["e"]?.toInt() ?: 0
        west = props["w"]?.toInt() ?: 0
        up = props["u"]?.toInt() ?: 0
        down = props["d"]?.toInt() ?: 0

        return RoomModel(id, name, description, north, south, east, west, up, down)
    }
}
