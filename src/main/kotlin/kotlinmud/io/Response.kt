package kotlinmud.io

import kotlinmud.action.model.ActionContextList

open class Response(
    val status: IOStatus,
    val actionContextList: ActionContextList,
    val message: Message,
    val delay: Int = 0
) {
    override fun toString(): String {
        return "message: toActionCreator: '${message.toActionCreator}', toTarget: '${message.toTarget}', toObservers: '${message.toObservers}'"
    }
}

fun createResponseWithEmptyActionContext(message: Message, status: IOStatus = IOStatus.OK): Response {
    return Response(status, ActionContextList(mutableListOf()), message)
}
