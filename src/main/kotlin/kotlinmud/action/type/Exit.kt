package kotlinmud.action.type

import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.type.Direction

typealias Exit = Map.Entry<Direction, RoomDAO>
