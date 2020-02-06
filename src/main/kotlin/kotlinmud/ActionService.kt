package kotlinmud

import kotlinmud.action.Action
import kotlinmud.action.createLookAction
import kotlinmud.action.createNorthAction
import kotlinmud.action.createSouthAction
import kotlinmud.io.Buffer
import kotlinmud.io.Response

class ActionService(private val eventService: EventService) {
    private val actions: List<Action> = arrayListOf(
        createLookAction(),
        createNorthAction(),
        createSouthAction()
    )

    fun run(buffer: Buffer): Response {
        val action = actions.find {
            it.command.toString().toLowerCase().startsWith(buffer.getCommand())
        } ?: return Response(buffer, "what was that?")
        if (!action.hasDisposition(buffer.getDisposition())) {
            return Response(buffer, "you are ${buffer.getDisposition()} and cannot do that.")
        }
        return action.mutator.invoke(eventService, buffer)
    }
}