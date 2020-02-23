package kotlinmud.io

import kotlinmud.action.ActionContextList

class EmptyResponse(
    actionContextList: ActionContextList = ActionContextList(mutableListOf()),
    message: Message = Message("", "")
) : Response(IOStatus.OK, actionContextList, message)
