package kotlinmud.persistence.model.builder

enum class RespawnType(val value: String) {
    Mob("mob"),
    Item("item"),
    Room("room");

    companion object {
        fun createFromString(value: String): RespawnType {
            return values().find { it.value == value }!!
        }
    }
}
