package kotlinmud.fs.loader.area.loader.reset

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.model.Model
import kotlinmud.fs.loader.area.model.reset.MobReset

class MobResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var mobId = 0
    var roomId = 0
    var maxInRoom = 0
    var maxInWorld = 0
    override var props: Map<String, String> = mapOf()

    override fun load(): Model {
        id++
        props = tokenizer.parseProperties()
        mobId = intAttr("mobId")
        roomId = intAttr("roomId")
        maxInRoom = intAttr("maxInRoom", 1)
        maxInWorld = intAttr("maxInWorld", 1)

        return MobReset(id, mobId, roomId, maxInRoom, maxInWorld)
    }
}
