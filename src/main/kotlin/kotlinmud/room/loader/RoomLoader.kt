package kotlinmud.room.loader

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.loader.strAttr
import kotlinmud.fs.loader.area.model.RoomModel
import kotlinmud.room.type.RegenLevel
import kotlinmud.world.BiomeType
import kotlinmud.world.ResourceType

class RoomLoader(private val tokenizer: Tokenizer, private val loadSchemaVersion: Int) : Loader {
    override fun load(): RoomModel {
        val id = tokenizer.parseInt()
        val name = tokenizer.parseString()
        val description = tokenizer.parseString()
        val area = tokenizer.parseString()
        val biomeType = BiomeType.fromString(tokenizer.parseString())
        val resources = tokenizer.parseString().trim().split(",").mapNotNull {
            val trimmed = it.trim()
            if (trimmed != "") ResourceType.fromString(trimmed) else null
        }.toList()
        val props = tokenizer.parseProperties()
        val regen = RegenLevel.valueOf(strAttr(props["regen"], "normal").toUpperCase())
        val isIndoor = strAttr(props["isIndoor"], "true").toBoolean()
        val north = props["n"] ?: ""
        val south = props["s"] ?: ""
        val east = props["e"] ?: ""
        val west = props["w"] ?: ""
        val up = props["u"] ?: ""
        val down = props["d"] ?: ""
        val ownerId = intAttr(props["ownerId"], 0)

        return RoomModel(id, name, description, regen, isIndoor, north, south, east, west, up, down, area, biomeType, resources, ownerId)
    }
}
