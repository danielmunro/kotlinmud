package kotlinmud.io.model

import kotlinmud.action.model.ActionContextList
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.type.IOStatus

class EmptyResponse(
    actionContextList: ActionContextList = ActionContextList(
        mutableListOf()
    ),
    message: Message = messageToActionCreator("")
) : Response(IOStatus.OK, actionContextList, message)
