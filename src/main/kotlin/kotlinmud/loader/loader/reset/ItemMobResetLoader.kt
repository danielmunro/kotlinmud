package kotlinmud.loader.loader.reset

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.loader.Loader
import kotlinmud.loader.model.Model
import kotlinmud.loader.model.reset.ItemMobReset

class ItemMobResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var itemId = 0
    var mobId = 0
    var maxInInventory = 0
    var maxInWorld = 0
    override var props: Map<String, String>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun load(): Model {
        id++
        val props = tokenizer.parseProperties()
        itemId = props["itemId"]!!.toInt()
        mobId = props["mobId"]!!.toInt()
        maxInInventory = props["maxInInventory"]!!.toInt()
        maxInWorld = props["maxInWorld"]!!.toInt()

        return ItemMobReset(id, itemId, mobId, maxInInventory, maxInWorld)
    }
}
