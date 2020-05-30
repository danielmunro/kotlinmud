package kotlinmud.fs.loader.area.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.model.RoomModel
import kotlinmud.world.BiomeType
import kotlinmud.world.ResourceType
import kotlinmud.world.room.RegenLevel

class RoomLoader(private val tokenizer: Tokenizer, private val loadSchemaVersion: Int) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var regen = RegenLevel.NORMAL
    var isIndoor = true
    var ownerId = 0
    var north = ""
    var south = ""
    var east = ""
    var west = ""
    var up = ""
    var down = ""
    var area = ""
    var biomeType = BiomeType.NONE
    var resources = listOf<ResourceType>()
    var props: Map<String, String> = mapOf()

    override fun load(): RoomModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        area = tokenizer.parseString()
        biomeType = BiomeType.fromString(tokenizer.parseString())
        resources = tokenizer.parseString().trim().split(",").mapNotNull {
            val trimmed = it.trim()
            if (trimmed != "") ResourceType.fromString(trimmed) else null
        }.toList()
        props = tokenizer.parseProperties()
        regen = RegenLevel.valueOf(strAttr(props["regen"], "normal").toUpperCase())
        isIndoor = strAttr(props["isIndoor"], "true").toBoolean()
        north = props["n"] ?: ""
        south = props["s"] ?: ""
        east = props["e"] ?: ""
        west = props["w"] ?: ""
        up = props["u"] ?: ""
        down = props["d"] ?: ""
        ownerId = intAttr(props["ownerId"], 0)

        return RoomModel(id, name, description, regen, isIndoor, north, south, east, west, up, down, area, biomeType, resources, ownerId)
    }
}
