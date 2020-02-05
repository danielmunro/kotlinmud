package kotlinmud

import kotlinmud.action.Action
import kotlinmud.action.createLookAction
import kotlinmud.action.createNorthAction
import kotlinmud.action.createSouthAction
import kotlinmud.io.Buffer
import kotlinmud.io.Response

class ActionService {
    private val actions: List<Action> = arrayListOf(
        createLookAction(),
        createNorthAction(),
        createSouthAction()
    )

    fun run(buffer: Buffer): Response {
        val action = actions.find {
            it.command.toString().toLowerCase().startsWith(buffer.getCommand())
        }
        return action?.mutator?.invoke(buffer) ?: Response(buffer, "what was that?")
    }
}