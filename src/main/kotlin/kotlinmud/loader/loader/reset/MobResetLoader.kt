package kotlinmud.loader.loader.reset

import kotlinmud.loader.Tokenizer
import kotlinmud.loader.loader.Loader
import kotlinmud.loader.model.Model
import kotlinmud.loader.model.reset.MobReset

class MobResetLoader(private val tokenizer: Tokenizer) : Loader {
    var id = 0
    var mobId = 0
    var roomId = 0
    var maxInRoom = 0
    var maxInWorld = 0

    override fun load(): Model {
        id++
        val props = tokenizer.parseProperties()
        mobId = props["mobId"]!!.toInt()
        roomId = props["roomId"]!!.toInt()
        maxInRoom = props["maxInRoom"]?.toInt() ?: 1
        maxInWorld = props["maxInWorld"]?.toInt() ?: 1

        return MobReset(id, mobId, roomId, maxInRoom, maxInWorld)
    }
}
