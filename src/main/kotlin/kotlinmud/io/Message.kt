package kotlinmud.io

class Message(
    val toActionCreator: String,
    val toTarget: String = "",
    val toObservers: String = toTarget
) {

    override fun toString(): String {
        return "to action creator: $toActionCreator, to target: $toTarget, to observers: $toObservers"
    }
}
