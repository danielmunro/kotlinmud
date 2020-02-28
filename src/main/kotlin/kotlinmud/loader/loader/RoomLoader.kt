package kotlinmud.loader.loader

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.RoomModel

class RoomLoader(private val tokenizer: Tokenizer) {
    var id = 0
    var name = ""
    var description = ""
    var north = ""
    var south = ""
    var east = ""
    var west = ""
    var up = ""
    var down = ""

    fun load(): RoomModel {
        id = tokenizer.parseId()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        val props = tokenizer.parseProperties()
        north = props["n"] ?: ""
        south = props["s"] ?: ""
        east = props["e"] ?: ""
        west = props["w"] ?: ""
        up = props["u"] ?: ""
        down = props["d"] ?: ""

        return RoomModel(id, name, description, north, south, east, west, up, down)
    }
}
