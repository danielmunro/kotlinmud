package kotlinmud

import kotlinmud.action.*
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.room.Room
import org.jetbrains.exposed.sql.transactions.transaction

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
        return action.mutator.invoke(eventService, buildContext(request, action), request)
    }

    private fun buildContext(request: Request, action: Action): ContextCollection {
        var i = 0
        return ContextCollection(action.syntax.map { createContext(it, findResult(request, it, request.args[i++])) } as MutableList<Context<Any>>)
    }

    private fun findResult(request: Request, syntax: Syntax, word: String): Any {
        when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> {
                val room = transaction { request.room.exits.find{ it.direction.toString().startsWith(word) } }?.destination
                if (room != null) {
                    return room
                }
            }
            Syntax.COMMAND -> request.getCommand()
            Syntax.NOOP -> {}
        }
        return "what was that?"
    }

    private fun <T> createContext(syntax: Syntax, result: T): Context<T> {
        return Context(syntax, Status.OK, result)
    }
}