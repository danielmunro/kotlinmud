package kotlinmud.event.response

import kotlinmud.event.EventResponse
import kotlinmud.room.RoomEntity

class InputReceivedResponse<T>(override val subject: T, val room: RoomEntity) : EventResponse<T>
