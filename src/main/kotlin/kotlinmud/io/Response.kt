package kotlinmud.io

import kotlinmud.action.ActionContextList

open class Response(val actionContextList: ActionContextList, val message: Message) {
    override fun toString(): String {
        return "message: toActionCreator: '${message.toActionCreator}', toTarget: '${message.toTarget}', toObservers: '${message.toObservers}'"
    }
}

fun createResponseWithEmptyActionContext(message: Message): Response {
    return Response(ActionContextList(mutableListOf()), message)
}
