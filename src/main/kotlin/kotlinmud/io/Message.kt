package kotlinmud.io

class Message(
    val toActionCreator: String,
    val toTarget: String = "",
    val toObservers: String = toTarget)