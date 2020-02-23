package kotlinmud.service

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.action.actions.*
import kotlinmud.action.contextBuilder.*
import kotlinmud.io.*
import kotlinmud.mob.Invokable
import kotlinmud.mob.Mob
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.skill.Skill
import kotlinmud.mob.skill.impl.Bash
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.impl.Bite
import kotlinmud.random.percentRoll

class ActionService(private val mobService: MobService, private val eventService: EventService) {
    private val actions: List<Action> = listOf(
        createLookAction(),
        createLookAtAction(),
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
        createSitAction(),
        createWakeAction(),
        createSleepAction())

    private val skills: List<Skill> = listOf(
        Bash(),
        Berserk(),
        Bite()
    )

    fun run(request: Request): Response {
        skills.find {
            it.type.toString().toLowerCase().startsWith(request.getCommand())
        }?.let {
            return dispositionCheck(request, it)
                ?: skillRoll(request.mob.skills[it.type] ?: error("no skill"))
                ?: callInvokable(request, it, buildActionContextList(request, it))
        }

        actions.find {
            it.command.value.startsWith(request.getCommand()) && it.syntax.size == request.args.size
        }?.let {
            return dispositionCheck(request, it)
                ?: callInvokable(request, it, buildActionContextList(request, it))
        } ?: return createResponseWithEmptyActionContext(Message("what was that?"))
    }

    private fun skillRoll(level: Int): Response? {
        return if (percentRoll() < level) null else createResponseWithEmptyActionContext(
            Message("You lost your concentration."), IOStatus.FAILED)
    }

    private fun dispositionCheck(request: Request, requiresDisposition: RequiresDisposition): Response? {
        return if (!requiresDisposition.dispositions.contains(request.getDisposition()))
                createResponseWithEmptyActionContext(Message("you are ${request.getDisposition().value} and cannot do that."))
            else
                null
    }

    private fun callInvokable(request: Request, invokable: Invokable, list: ActionContextList): Response {
        val error = list.getError()
        if (error != null) {
            return createResponseWithEmptyActionContext(Message(error.result as String))
        }
        with(invokable.invoke(ActionContextService(mobService, eventService, list), request)) {
            return if (invokable is Action && invokable.isChained())
                run(createChainToRequest(request.mob, invokable))
            else
                this
        }
    }

    private fun createChainToRequest(mob: Mob, action: Action): Request {
        return Request(mob, action.chainTo.toString(), mobService.getRoomForMob(mob))
    }

    private fun buildActionContextList(request: Request, invokable: Invokable): ActionContextList {
        var i = 0
        return ActionContextList(invokable.syntax.map { createContext(request, it, request.args[i++]) } as MutableList<Context<Any>>)
    }

    private fun createContext(request: Request, syntax: Syntax, word: String): Context<Any> {
        return when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.room).build(syntax, word)
            Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(request.mob).build(syntax, word)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.TARGET_MOB -> TODO()
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
    }
}
