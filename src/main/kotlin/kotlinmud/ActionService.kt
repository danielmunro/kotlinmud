package kotlinmud

import kotlinmud.action.Action
import kotlinmud.action.createLookAction
import kotlinmud.action.createNorthAction
import kotlinmud.action.createSouthAction

class ActionService {
    private val actions: List<Action> = arrayListOf(
        createLookAction(),
        createNorthAction(),
        createSouthAction()
    )
}