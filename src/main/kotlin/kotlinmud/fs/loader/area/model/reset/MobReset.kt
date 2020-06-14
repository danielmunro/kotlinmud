package kotlinmud.fs.loader.area.model.reset

import kotlinmud.data.Row
import kotlinmud.fs.loader.area.model.Model

data class MobReset(
    override val id: Int,
    val mobId: Int,
    val roomId: Int,
    val maxInRoom: Int,
    val maxInWorld: Int
) : Model, Row
