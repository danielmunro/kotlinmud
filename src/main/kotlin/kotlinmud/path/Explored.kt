package kotlinmud.path

import kotlinmud.room.dao.RoomDAO

class Explored(val room: RoomDAO, var explored: Boolean = false)
