package kotlinmud.persistence.exception

class RoomConnectionException(missingRoomId: Int) : Exception("room id $missingRoomId not found!")
