package kotlinmud.loader.loader

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.RoomModel
import kotlinmud.room.RegenLevel

class RoomLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var regen = RegenLevel.NORMAL
    var isIndoor = true
    var north = ""
    var south = ""
    var east = ""
    var west = ""
    var up = ""
    var down = ""
    override var props: Map<String, String> = mapOf()

    override fun load(): RoomModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        props = tokenizer.parseProperties()
        regen = RegenLevel.valueOf(strAttr("regen", "normal").toUpperCase())
        isIndoor = strAttr("isIndoor", "true").toBoolean()
        north = props["n"] ?: ""
        south = props["s"] ?: ""
        east = props["e"] ?: ""
        west = props["w"] ?: ""
        up = props["u"] ?: ""
        down = props["d"] ?: ""

        return RoomModel(id, name, description, regen, isIndoor, north, south, east, west, up, down)
    }
}
