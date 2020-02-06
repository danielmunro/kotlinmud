package kotlinmud

import kotlinmud.action.Action
import kotlinmud.action.createLookAction
import kotlinmud.action.createNorthAction
import kotlinmud.action.createSouthAction
import kotlinmud.io.Request
import kotlinmud.io.Response

class ActionService(private val eventService: EventService) {
    private val actions: List<Action> = arrayListOf(
        createLookAction(),
        createNorthAction(),
        createSouthAction()
    )

    fun run(request: Request): Response {
        val action = actions.find {
            it.command.toString().toLowerCase().startsWith(request.getCommand())
        } ?: return Response(request, "what was that?")
        if (!action.hasDisposition(request.getDisposition())) {
            return Response(request, "you are ${request.getDisposition()} and cannot do that.")
        }
        return action.mutator.invoke(eventService, request)
    }
}