package kotlinmud.service

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.action.actions.*
import kotlinmud.action.contextBuilder.*
import kotlinmud.io.*
import kotlinmud.mob.MobEntity

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
        createInventoryAction(),
        createKillAction(),
        createSitAction())

    fun run(request: Request): Response {
        val action = actions.find {
            it.command.value.startsWith(request.getCommand())
        } ?: return createResponseWithEmptyActionContext(Message("what was that?"))
        return dispositionCheck(request, action)
            ?: invokeActionMutator(request, action, buildActionContextList(request, action))
    }

    private fun dispositionCheck(request: Request, action: Action): Response? {
        return if (!action.hasDisposition(request.getDisposition()))
                createResponseWithEmptyActionContext(Message("you are ${request.getDisposition().value} and cannot do that."))
            else
                null
    }

    private fun invokeActionMutator(request: Request, action: Action, list: ActionContextList): Response {
        val error = list.getError()
        if (error != null) {
            return createResponseWithEmptyActionContext(Message(error.result as String))
        }
        with(action.mutator.invoke(actionContextService, list, request)) {
            return if (action.isChained())
                run(createChainToRequest(request.mob, action))
            else
                this
        }
    }

    private fun createChainToRequest(mob: MobEntity, action: Action): Request {
        return Request(mob, action.chainTo.toString(), mobService.getRoomForMob(mob))
    }

    private fun buildActionContextList(request: Request, action: Action): ActionContextList {
        var i = 0
        return ActionContextList(action.syntax.map { createContext(request, it, request.args[i++]) } as MutableList<Context<Any>>)
    }

    private fun createContext(request: Request, syntax: Syntax, word: String): Context<Any> {
        return when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.room).build(syntax, word)
            Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(request.mob).build(syntax, word)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
    }
}
