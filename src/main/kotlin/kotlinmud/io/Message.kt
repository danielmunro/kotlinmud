package kotlinmud.io

data class Message(
    val toActionCreator: String,
    val toTarget: String = "",
    val toObservers: String = toTarget
)
