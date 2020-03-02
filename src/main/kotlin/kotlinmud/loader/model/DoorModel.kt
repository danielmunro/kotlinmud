package kotlinmud.loader.model

import kotlinmud.room.exit.DoorDisposition

class DoorModel(override val id: Int, val name: String, val description: String, var disposition: DoorDisposition, val keyItemId: Int? = null) : Model
