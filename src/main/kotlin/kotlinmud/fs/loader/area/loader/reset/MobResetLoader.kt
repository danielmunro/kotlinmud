package kotlinmud.fs.loader.area.loader.reset

import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.Loader
import kotlinmud.fs.loader.area.loader.intAttr
import kotlinmud.fs.loader.area.model.Model
import kotlinmud.fs.loader.area.model.reset.MobReset

class MobResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var mobId = 0
    var roomId = 0
    var maxInRoom = 0
    var maxInWorld = 0

    override fun load(): Model {
        id++
        val props = tokenizer.parseProperties()
        mobId = intAttr(props["mobId"])
        roomId = intAttr(props["roomId"])
        maxInRoom = intAttr(props["maxInRoom"], 1)
        maxInWorld = intAttr(props["maxInWorld"], 1)

        return MobReset(id, mobId, roomId, maxInRoom, maxInWorld)
    }
}
