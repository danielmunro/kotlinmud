package kotlinmud.room.exit

enum class DoorDisposition {
    CLOSED,
    OPEN,
    LOCKED,
}

fun getDoorDispositionFromString(disposition: String): DoorDisposition {
    return when(disposition) {
        "closed" -> DoorDisposition.CLOSED
        "open" -> DoorDisposition.OPEN
        "locked" -> DoorDisposition.LOCKED
        else -> error("no door disposition: '$disposition'")
    }
}
