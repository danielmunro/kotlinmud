package kotlinmud.action

import kotlinmud.action.contextBuilder.AvailableDrinkContextBuilder
import kotlinmud.action.contextBuilder.AvailableFoodContextBuilder
import kotlinmud.action.contextBuilder.AvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.AvailableNounContextBuilder
import kotlinmud.action.contextBuilder.CastContextBuilder
import kotlinmud.action.contextBuilder.CommandContextBuilder
import kotlinmud.action.contextBuilder.DirectionToExitContextBuilder
import kotlinmud.action.contextBuilder.DirectionWithNoExitContextBuilder
import kotlinmud.action.contextBuilder.DoorInRoomContextBuilder
import kotlinmud.action.contextBuilder.EquipmentInInventoryContextBuilder
import kotlinmud.action.contextBuilder.EquippedItemContextBuilder
import kotlinmud.action.contextBuilder.FreeFormContextBuilder
import kotlinmud.action.contextBuilder.ItemFromMerchantContextBuilder
import kotlinmud.action.contextBuilder.ItemInAvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInRoomContextBuilder
import kotlinmud.action.contextBuilder.ItemToSellContextBuilder
import kotlinmud.action.contextBuilder.MobInRoomContextBuilder
import kotlinmud.action.contextBuilder.OptionalFurnitureContextBuilder
import kotlinmud.action.contextBuilder.OptionalTargetContextBuilder
import kotlinmud.action.contextBuilder.PlayerMobContextBuilder
import kotlinmud.action.contextBuilder.RecipeContextBuilder
import kotlinmud.action.contextBuilder.ResourceInRoomContextBuilder
import kotlinmud.action.contextBuilder.SkillToPracticeContextBuilder
import kotlinmud.action.contextBuilder.SpellContextBuilder
import kotlinmud.action.contextBuilder.SpellFromHealerContextBuilder
import kotlinmud.action.contextBuilder.TargetMobContextBuilder
import kotlinmud.action.contextBuilder.TrainableContextBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.model.ActionContextList
import kotlinmud.action.model.Context
import kotlinmud.action.type.Command
import kotlinmud.action.type.Status
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.IOStatus
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.io.createResponseWithEmptyActionContext
import kotlinmud.io.messageToActionCreator
import kotlinmud.item.HasInventory
import kotlinmud.item.ItemService
import kotlinmud.item.createRecipeList
import kotlinmud.math.percentRoll
import kotlinmud.mob.HasCosts
import kotlinmud.mob.MobService
import kotlinmud.mob.RequiresDisposition
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.CostType
import kotlinmud.mob.skill.SkillAction
import kotlinmud.mob.skill.createSkillList
import kotlinmud.mob.type.Intent
import kotlinmud.player.PlayerService
import org.slf4j.LoggerFactory

fun commandMatches(command: Command, input: String): Boolean {
    return command.value.startsWith(input)
}

fun argumentLengthMatches(syntax: List<Syntax>, arguments: List<String>): Boolean {
    return syntax.size == arguments.size || syntax.contains(Syntax.FREE_FORM)
}

fun subCommandMatches(syntax: Syntax, subCommand: String, input: String): Boolean {
    return syntax != Syntax.SUBCOMMAND || subCommand.startsWith(input)
}

class ActionService(
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val itemService: ItemService,
    private val actionContextBuilder: (request: Request, actionContextList: ActionContextList) -> ActionContextService,
    private val actions: List<Action>
) {
    private val skills = createSkillList()
    private val recipes = createRecipeList()
    private val logger = LoggerFactory.getLogger(ActionService::class.java)

    fun run(request: Request): Response {
        if (request.input == "") {
            return createResponseWithEmptyActionContext(messageToActionCreator(""))
        }
        return runSkill(request)
            ?: runAction(request)
            ?: createResponseWithEmptyActionContext(
                messageToActionCreator("what was that?"),
                IOStatus.ERROR
            )
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
            val parts = it.command.value.split(" ")
            val subPart = if (parts.size > 1) parts[1] else ""
            val syntax = if (it.syntax.size > 1) it.syntax[1] else Syntax.NOOP
            commandMatches(it.command, request.getCommand()) &&
                    argumentLengthMatches(it.syntax, request.args) &&
                    subCommandMatches(syntax, subPart, request.getSubject())
        }?.let {
            val contextList = buildActionContextList(request, it)
            dispositionCheck(request, it)
                ?: checkForBadContext(contextList)
                ?: deductCosts(request.mob, it)
                ?: callInvokable(request, it, contextList)
        }
    }

    private fun executeSkill(request: Request, skill: SkillAction): Response {
        return dispositionCheck(request, skill)
            ?: deductCosts(request.mob, skill)
            ?: skillRoll(request.mob.skills[skill.type] ?: error("no skill"))
            ?: callInvokable(request, skill, buildActionContextList(request, skill))
    }

    private fun deductCosts(mob: Mob, hasCosts: HasCosts): Response? {
        val cost = hasCosts.costs.find {
            when (it.type) {
                CostType.MV_AMOUNT -> mob.mv < it.amount
                CostType.MV_PERCENT -> mob.mv < Math.max(mob.calc(Attribute.MV) * (it.amount.toDouble() / 100), 50.0)
                CostType.MANA_AMOUNT -> mob.mana < it.amount
                CostType.MANA_PERCENT -> mob.mana < mob.calc(Attribute.MANA) * (it.amount.toDouble() / 100)
                else -> false
            }
        }
        if (cost != null) {
            return createResponseWithEmptyActionContext(
                messageToActionCreator("You are too tired")
            )
        }
        hasCosts.costs.forEach {
            when (it.type) {
                CostType.DELAY -> return@forEach
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
            messageToActionCreator("You lost your concentration."),
            IOStatus.FAILED
        )
    }

    private fun dispositionCheck(request: Request, requiresDisposition: RequiresDisposition): Response? {
        return if (!requiresDisposition.dispositions.contains(request.getDisposition()))
                createResponseWithEmptyActionContext(
                    messageToActionCreator("you are ${request.getDisposition().value} and cannot do that.")
                )
            else
                null
    }

    private fun callInvokable(request: Request, invokable: Invokable, list: ActionContextList): Response {
        return checkForBadContext(list) ?: with(invokable.invoke(actionContextBuilder(request, list))) {
            if (invokable is Action && invokable.isChained())
                run(createChainToRequest(request.mob, invokable))
            else
                this
        }
    }

    private fun checkForBadContext(contextList: ActionContextList): Response? {
        return contextList.getBadResult()?.let {
            createResponseWithEmptyActionContext(
                messageToActionCreator(it.result as String),
                IOStatus.ERROR
            )
        }
    }

    private fun createChainToRequest(mob: Mob, action: Action): Request {
        return Request(mob, action.chainTo.toString(), mobService.getRoomForMob(mob))
    }

    private fun buildActionContextList(request: Request, invokable: Invokable): ActionContextList {
        logger.debug("${request.mob} building action context :: {}, {}", invokable.command, invokable.syntax)
        var i = 0
        var previous: Context<Any> =
            Context(Syntax.NOOP, Status.OK, request)
        return ActionContextList(invokable.syntax.map {
            val current = createContext(request, it, if (request.args.size > i) request.args[i++] else "", previous)
            previous = current
            current
        } as MutableList<Context<Any>>)
    }

    private fun createContext(request: Request, syntax: Syntax, word: String, previous: Context<Any>): Context<Any> {
        return when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.room).build(syntax, word)
            Syntax.DIRECTION_WITH_NO_EXIT -> DirectionWithNoExitContextBuilder(request.room).build(syntax, word)
            Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.SUBCOMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(itemService, request.mob).build(syntax, word)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(itemService, request.room).build(syntax, word)
            Syntax.EQUIPMENT_IN_INVENTORY -> EquipmentInInventoryContextBuilder(itemService, request.mob).build(syntax, word)
            Syntax.EQUIPPED_ITEM -> EquippedItemContextBuilder(request.mob).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, itemService, request.mob, request.room).build(syntax, word)
            Syntax.TARGET_MOB -> TargetMobContextBuilder(mobService, request.mob, request.room).build(syntax, word)
            Syntax.OPTIONAL_TARGET -> OptionalTargetContextBuilder(request.mob, mobService.getMobsForRoom(request.room) + itemService.findAllByOwner(request.mob)).build(syntax, word)
            Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.FREE_FORM -> FreeFormContextBuilder(request.args).build(syntax, word)
            Syntax.ITEM_FROM_MERCHANT -> ItemFromMerchantContextBuilder(itemService, request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.ITEM_TO_SELL -> ItemToSellContextBuilder(itemService, request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.SPELL -> SpellContextBuilder(skills).build(syntax, word)
            Syntax.SPELL_FROM_HEALER -> SpellFromHealerContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
            Syntax.CAST -> CastContextBuilder().build(syntax, word)
            Syntax.PLAYER_MOB -> PlayerMobContextBuilder(mobService).build(syntax, word)
            Syntax.AVAILABLE_DRINK -> AvailableDrinkContextBuilder(itemService, request.mob, request.room).build(syntax, word)
            Syntax.AVAILABLE_FOOD -> AvailableFoodContextBuilder(itemService, request.mob).build(syntax, word)
            Syntax.TRAINABLE -> TrainableContextBuilder(mobService, playerService, request.mob).build(syntax, word)
            Syntax.SKILL_TO_PRACTICE -> SkillToPracticeContextBuilder(playerService, request.mob).build(syntax, word)
            Syntax.RECIPE -> RecipeContextBuilder(recipes).build(syntax, word)
            Syntax.RESOURCE_IN_ROOM -> ResourceInRoomContextBuilder(request.room).build(syntax, word)
            Syntax.AVAILABLE_ITEM_INVENTORY -> AvailableItemInventoryContextBuilder(request.mob, request.room, itemService).build(syntax, word)
            Syntax.ITEM_IN_AVAILABLE_INVENTORY -> ItemInAvailableItemInventoryContextBuilder(itemService, previous.result as HasInventory).build(syntax, word)
            Syntax.OPTIONAL_FURNITURE -> OptionalFurnitureContextBuilder(itemService.findAllByOwner(request.room)).build(syntax, word)
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
    }
}
