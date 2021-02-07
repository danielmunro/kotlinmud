package kotlinmud.action.exception

import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import java.lang.Exception

class InvokeException(message: String) : Exception(message) {
    fun toMessage(): Message {
        return MessageBuilder().toActionCreator(message!!).build()
    }
}
