package kotlinmud

import kotlinmud.action.*
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

class ActionService(private val mobService: MobService, private val eventService: EventService) {
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
        val contextCollection = buildContext(request, action)
        val error = contextCollection.getError()
        if (error != null) {
            return Response(request, error.result as String)
        }
        val response = action.mutator.invoke(eventService, contextCollection, request)
        if (action.chainTo != Command.NOOP) {
            return run(
                Request(
                    request.client,
                    action.chainTo.toString(),
                    mobService.getRoomForMob(request.mob)))
        }
        return response
    }

    private fun buildContext(request: Request, action: Action): ContextCollection {
        var i = 0
        return ContextCollection(action.syntax.map { createContext(request, it, request.args[i++]) } as MutableList<Context<Any>>)
    }

    private fun createContext(request: Request, syntax: Syntax, word: String): Context<Any> {
        return when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> {
                val room = transaction {
                    request.room.exits.find{ it.direction.toString().toLowerCase().startsWith(word) }?.destination
                }
                if (room != null) {
                    return Context(syntax, Status.OK, room)
                }
                return Context(syntax, Status.FAILED, "Alas, that direction does not exist.")
            }
            Syntax.COMMAND -> Context(syntax, Status.OK, request.getCommand())
            Syntax.NOOP -> Context(syntax, Status.OK, "What was that?")
        }
    }
}