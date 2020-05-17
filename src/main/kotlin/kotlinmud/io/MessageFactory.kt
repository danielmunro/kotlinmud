package kotlinmud.io

fun messageToActionCreator(message: String): Message {
    return MessageBuilder()
        .toActionCreator(message)
        .build()
}
