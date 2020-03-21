package kotlinmud.service

import kotlinmud.action.Action
import kotlinmud.action.ActionContextList
import kotlinmud.action.ActionContextService
import kotlinmud.action.Context
import kotlinmud.action.Status
import kotlinmud.action.contextBuilder.AvailableDrinkContextBuilder
import kotlinmud.action.contextBuilder.AvailableFoodContextBuilder
import kotlinmud.action.contextBuilder.AvailableNounContextBuilder
import kotlinmud.action.contextBuilder.CastContextBuilder
import kotlinmud.action.contextBuilder.CommandContextBuilder
import kotlinmud.action.contextBuilder.DirectionToExitContextBuilder
import kotlinmud.action.contextBuilder.DoorInRoomContextBuilder
import kotlinmud.action.contextBuilder.EquipmentInInventoryContextBuilder
import kotlinmud.action.contextBuilder.EquippedItemContextBuilder
import kotlinmud.action.contextBuilder.FreeFormContextBuilder
import kotlinmud.action.contextBuilder.ItemFromMerchantContextBuilder
import kotlinmud.action.contextBuilder.ItemInInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInRoomContextBuilder
import kotlinmud.action.contextBuilder.ItemToSellContextBuilder
import kotlinmud.action.contextBuilder.MobInRoomContextBuilder
import kotlinmud.action.contextBuilder.OptionalTargetContextBuilder
import kotlinmud.action.contextBuilder.PlayerMobContextBuilder
import kotlinmud.action.contextBuilder.SpellContextBuilder
import kotlinmud.action.contextBuilder.TargetMobContextBuilder
import kotlinmud.action.createActionsList
import kotlinmud.attributes.Attribute
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.math.random.percentRoll
import kotlinmud.mob.Intent
import kotlinmud.mob.Invokable
import kotlinmud.mob.Mob
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.skill.CostType
import kotlinmud.mob.skill.Skill
import kotlinmud.mob.skill.SkillAction
import kotlinmud.mob.skill.createSkillList

class ActionService(private val mobService: MobService, private val eventService: EventService) {
    private val actions: List<Action> = createActionsList()
    private val skills: List<Skill> = createSkillList()

    fun run(request: Request): Response {
        return runSkill(request)
            ?: runAction(request)
            ?: createResponseWithEmptyActionContext(
                Message("what was that?"), IOStatus.ERROR)
    }

    private fun runSkill(request: Request): Response? {
        val skill = (skills.find {
            it is SkillAction && it.matchesRequest(request)
        } ?: return null) as SkillAction

        val response = executeSkill(request, skill)

        if (skill.intent == Intent.OFFENSIVE) {
            triggerFightForOffensiveSkills(
                request.mob,
                response.actionContextList.getResultBySyntax(Syntax.TARGET_MOB)
            )
        }
        return response
    }

    private fun runAction(request: Request): Response? {
        return actions.find {
            it.command.value.startsWith(request.getCommand()) &&
                    (it.syntax.size == request.args.size || it.syntax.contains(Syntax.FREE_FORM))
        }?.let {
            dispositionCheck(request, it)
                ?: callInvokable(request, it, buildActionContextList(request, it))
        }
    }

    private fun executeSkill(request: Request, skill: SkillAction): Response {
        return dispositionCheck(request, skill)
            ?: deductCosts(request.mob, skill)
            ?: skillRoll(request.mob.skills[skill.type] ?: error("no skill"))
            ?: callInvokable(request, skill, buildActionContextList(request, skill))
    }

    private fun deductCosts(mob: Mob, skill: Skill): Response? {
        val cost = skill.costs.find {
            when (it.type) {
                CostType.MV_AMOUNT -> mob.mv < it.amount
                CostType.MV_PERCENT -> mob.mv < Math.max(mob.calc(Attribute.MV) * (it.amount.toDouble() / 100), 50.0)
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

    private fun triggerFightForOffensiveSkills(mob: Mob, target: Mob) {
        mobService.findFightForMob(mob) ?: mobService.addFight(Fight(mob, target))
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
        return list.getBadResult()?.let {
            createResponseWithEmptyActionContext(Message(it.result as String), IOStatus.ERROR)
        } ?: with(invokable.invoke(ActionContextService(mobService, eventService, list), request)) {
            if (invokable is Action && invokable.isChained())
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
            Syntax.EQUIPMENT_IN_INVENTORY -> EquipmentInInventoryContextBuilder(request.mob).build(syntax, word)
            Syntax.EQUIPPED_ITEM -> EquippedItemContextBuilder(request.mob).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.TARGET_MOB -> TargetMobContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.OPTIONAL_TARGET -> OptionalTargetContextBuilder(request.mob, mobService.getMobsForRoom(request.room) + request.mob.inventory.items).build(syntax, word)
            Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.FREE_FORM -> FreeFormContextBuilder(request.args).build(syntax, word)
            Syntax.ITEM_FROM_MERCHANT -> ItemFromMerchantContextBuilder(request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.ITEM_TO_SELL -> ItemToSellContextBuilder(request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.SPELL -> SpellContextBuilder(skills).build(syntax, word)
            Syntax.CAST -> CastContextBuilder().build(syntax, word)
            Syntax.PLAYER_MOB -> PlayerMobContextBuilder(mobService).build(syntax, word)
            Syntax.AVAILABLE_DRINK -> AvailableDrinkContextBuilder(request.mob, request.room).build(syntax, word)
            Syntax.AVAILABLE_FOOD -> AvailableFoodContextBuilder(request.mob).build(syntax, word)
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
    }
}
