package kotlinmud.service

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.action.contextBuilder.*
import kotlinmud.action.impl.*
import kotlinmud.attributes.Attribute
import kotlinmud.io.*
import kotlinmud.mob.Intent
import kotlinmud.mob.Invokable
import kotlinmud.mob.Mob
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.skill.CostType
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
        createFleeAction(),
        createSitAction(),
        createWakeAction(),
        createSleepAction(),
        createOpenAction(),
        createCloseAction())

    private val skills: List<Skill> = listOf(
        Bash(),
        Berserk(),
        Bite()
    )

    fun run(request: Request): Response {
        return runSkill(request)
            ?: runAction(request)
            ?: createResponseWithEmptyActionContext(
                Message("what was that?"), IOStatus.ERROR)
    }

    private fun runSkill(request: Request): Response? {
        val skill = skills.find {
            it.type.toString().toLowerCase().startsWith(request.getCommand())
        } ?: return null
        val response = dispositionCheck(request, skill)
            ?: deductCosts(request.mob, skill)
            ?: skillRoll(request.mob.skills[skill.type] ?: error("no skill"))
            ?: callInvokable(request, skill, buildActionContextList(request, skill))
        if (skill.intent == Intent.OFFENSIVE) {
            val target: Mob = response.actionContextList.getResultBySyntax(Syntax.TARGET_MOB)
            mobService.findFightForMob(request.mob)
                ?: mobService.addFight(Fight(request.mob, target))
            mobService.findFightForMob(target)
                ?: mobService.addFight(Fight(target, request.mob))
        }
        return response
    }

    private fun runAction(request: Request): Response? {
        return actions.find {
            it.command.value.startsWith(request.getCommand()) && it.syntax.size == request.args.size
        }?.let {
            dispositionCheck(request, it)
                ?: callInvokable(request, it, buildActionContextList(request, it))
        }
    }

    private fun deductCosts(mob: Mob, skill: Skill): Response? {
        val cost = skill.costs.find {
            when (it.type) {
                CostType.MV_AMOUNT -> mob.mv < it.amount
                CostType.MV_PERCENT -> mob.mv < mob.calc(Attribute.MV) * (it.amount.toDouble() / 100)
                CostType.MANA_AMOUNT -> mob.mana < it.amount
                CostType.MANA_PERCENT -> mob.mana < mob.calc(Attribute.MANA) * (it.amount.toDouble() / 100)
                else -> false
            }
        }
        if (cost != null) {
            return createResponseWithEmptyActionContext(Message("You are too tired"))
        }
        skill.costs.forEach {
            when (it.type) {
                CostType.DELAY -> mob.delay += it.amount
                CostType.MV_AMOUNT -> mob.mv -= it.amount
                CostType.MV_PERCENT -> mob.mv -= (mob.calc(Attribute.MV) * (it.amount.toDouble() / 100)).toInt()
                CostType.MANA_AMOUNT -> mob.mana -= it.amount
                CostType.MANA_PERCENT -> mob.mana -= (mob.calc(Attribute.MANA) * (it.amount.toDouble() / 100)).toInt()
            }
        }
        return null
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
            return createResponseWithEmptyActionContext(Message(error.result as String), IOStatus.ERROR)
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
        return ActionContextList(invokable.syntax.map { createContext(request, it, if (request.args.size > i) request.args[i++] else "") } as MutableList<Context<Any>>)
    }

    private fun createContext(request: Request, syntax: Syntax, word: String): Context<Any> {
        return when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.room).build(syntax, word)
            Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(request.mob).build(syntax, word)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.TARGET_MOB -> TargetMobContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
    }
}
