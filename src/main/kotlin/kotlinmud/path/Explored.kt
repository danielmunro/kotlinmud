package kotlinmud.path

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room

class Explored(val room: RoomDAO, var explored: Boolean = false)
