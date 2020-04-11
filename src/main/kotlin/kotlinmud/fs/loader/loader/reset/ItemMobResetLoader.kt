package kotlinmud.fs.loader.loader.reset

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.loader.Loader
import kotlinmud.fs.loader.model.Model
import kotlinmud.fs.loader.model.reset.ItemMobReset

class ItemMobResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var itemId = 0
    var mobId = 0
    var maxInInventory = 0
    var maxInWorld = 0
    override var props: Map<String, String>
        get() = TODO("Not yet implemented")
        set(_) {}

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
