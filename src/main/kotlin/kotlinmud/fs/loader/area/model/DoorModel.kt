package kotlinmud.fs.loader.area.model

import kotlinmud.room.type.DoorDisposition

class DoorModel(override val id: Int, val name: String, val description: String, var disposition: DoorDisposition, val keyItemId: Int? = null) : Model
