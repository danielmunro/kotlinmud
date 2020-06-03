package kotlinmud.io.factory

import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder

fun messageToActionCreator(message: String): Message {
    return MessageBuilder()
        .toActionCreator(message)
        .build()
}
