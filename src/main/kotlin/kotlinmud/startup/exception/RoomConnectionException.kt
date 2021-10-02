package kotlinmud.startup.exception

class RoomConnectionException(missingRoomId: Int) : Exception("room id $missingRoomId not found!")
