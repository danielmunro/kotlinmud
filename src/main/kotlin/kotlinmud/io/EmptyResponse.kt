package kotlinmud.io

import kotlinmud.action.ActionContextList

class EmptyResponse(
    actionContextList: ActionContextList = ActionContextList(mutableListOf()),
    message: Message = messageToActionCreator("")
) : Response(IOStatus.OK, actionContextList, message)
