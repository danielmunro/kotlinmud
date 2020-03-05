package kotlinmud.loader.model.reset

import kotlinmud.loader.model.Model

class MobReset(override val id: Int, val mobId: Int, val roomId: Int, val maxInRoom: Int, val maxInWorld: Int) : Model
