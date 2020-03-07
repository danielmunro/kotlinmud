package kotlinmud.loader.loader

import kotlinmud.attributes.Attributes
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.ItemModel

class ItemLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var value = 0

    override fun load(): ItemModel {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        val props = tokenizer.parseProperties()
        return ItemModel(
            id,
            name,
            description,
            props["value"]?.toInt() ?: 1,
            props["level"]?.toInt() ?: 1,
            props["weight"]?.toDouble() ?: 1.0,
            Attributes(),
            if (props["material"] != null) Material.valueOf(props["material"]!!.toUpperCase()) else Material.ORGANIC,
            if (props["position"] != null) Position.valueOf(props["position"]!!.toUpperCase()) else Position.NONE
        )
    }
}
