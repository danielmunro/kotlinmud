package kotlinmud.io

import kotlinmud.action.ActionContextList

class Response(private val request: Request, val actionContextList: ActionContextList, val message: Message)

fun createResponseWithEmptyActionContext(request: Request, message: Message): Response {
    return Response(request, ActionContextList(mutableListOf()), message)
}
