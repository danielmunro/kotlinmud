package kotlinmud.loader.loader

import kotlinmud.item.Item
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.Tokenizer
import kotlinmud.loader.model.Model

class ItemLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var name = ""
    var description = ""
    var value = 0
    override var props: Map<String, String> = mapOf()

    override fun load(): Model {
        id = tokenizer.parseInt()
        name = tokenizer.parseString()
        description = tokenizer.parseString()
        props = tokenizer.parseProperties()

        return Item.Builder(id, name)
            .setDescription(description)
            .setValue(props["value"]?.toInt() ?: 1)
            .setLevel(props["level"]?.toInt() ?: 1)
            .setWeight(props["weight"]?.toDouble() ?: 1.0)
            .setMaterial(Material.valueOf(strAttr("material", "organic").toUpperCase()))
            .setPosition(Position.valueOf(strAttr("position", "none").toUpperCase()))
    }
}
