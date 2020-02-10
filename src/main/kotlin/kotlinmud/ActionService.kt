package kotlinmud

import kotlinmud.action.*
import kotlinmud.action.actions.*
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import org.jetbrains.exposed.sql.transactions.transaction

class ActionService(private val mobService: MobService, eventService: EventService) {
    private val actionContextService = ActionContextService(mobService, eventService)
    private val actions: List<Action> = arrayListOf(
        createLookAction(),
        createNorthAction(),
        createSouthAction(),
        createEastAction(),
        createWestAction(),
        createUpAction(),
        createDownAction(),
        createGetAction(),
        createDropAction(),
        createInventoryAction())

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
        val response = action.mutator.invoke(actionContextService, contextCollection, request)
        if (action.chainTo != Command.NOOP) {
            return run(
                Request(
                    request.mob,
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
            Syntax.ITEM_IN_INVENTORY -> {
                return transaction {
                    request.mob.inventory.items.find{ matches(it.name, request.getTarget()) }
                        ?.let { Context<Any>(syntax, Status.OK, it) } ?:
                    Context<Any>(syntax, Status.FAILED, "you don't have that.")
                }
            }
            Syntax.ITEM_IN_ROOM -> {
                return transaction {
                    request.room.inventory.items.find{ matches(it.name, request.getTarget()) }
                        ?.let { Context<Any>(syntax, Status.OK, it) } ?:
                    Context<Any>(syntax, Status.FAILED, "you don't see that anywhere.")
                }
            }
            Syntax.NOOP -> Context(syntax, Status.OK, "What was that?")
        }
    }
}

fun matches(name: String, input: String): Boolean {
    return name.split(" ").any { it.length > 1 && it.startsWith(input) }
}