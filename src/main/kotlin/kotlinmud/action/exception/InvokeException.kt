package kotlinmud.action.exception

import java.lang.Exception
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder

class InvokeException(message: String) : Exception(message) {
    fun toMessage(): Message {
        return MessageBuilder().toActionCreator(message!!).build()
    }
}
