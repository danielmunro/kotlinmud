package kotlinmud.loader.model

import kotlinmud.room.exit.DoorDisposition

class Door(val name: String, val description: String, var disposition: DoorDisposition, val keyItemId: Int? = null)