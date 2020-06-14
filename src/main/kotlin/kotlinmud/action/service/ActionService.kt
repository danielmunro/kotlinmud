package kotlinmud.action.service

import kotlinmud.action.factory.createContextFromSyntax
import kotlinmud.action.model.Action
import kotlinmud.action.model.ActionContextList
import kotlinmud.action.model.Context
import kotlinmud.action.type.Command
import kotlinmud.action.type.Invokable
import kotlinmud.action.type.Status
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.math.percentRoll
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.SkillAction
import kotlinmud.mob.skill.createSkillList
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.type.HasCosts
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.RequiresDisposition
import kotlinmud.player.service.PlayerService
import org.slf4j.LoggerFactory

class ActionService(
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val itemService: ItemService,
    private val actionContextBuilder: (request: Request, actionContextList: ActionContextList) -> ActionContextService,
    private val actions: List<Action>
) {
    companion object {
        private fun commandMatches(command: Command, input: String): Boolean {
            return command.value.startsWith(input)
        }

        private fun argumentLengthMatches(syntax: List<Syntax>, arguments: List<String>): Boolean {
            return syntax.size == arguments.size || syntax.contains(Syntax.FREE_FORM)
        }

        private fun subCommandMatches(syntax: Syntax, subCommand: String, input: String): Boolean {
            return syntax != Syntax.SUBCOMMAND || subCommand.startsWith(input)
        }
    }

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
        return Request(
            mob,
            action.chainTo.toString(),
            mobService.getRoomForMob(mob)
        )
    }

    private fun buildActionContextList(request: Request, invokable: Invokable): ActionContextList {
        logger.debug("${request.mob} building action context :: {}, {}", invokable.command, invokable.syntax)
        var i = 0
        var previous: Context<Any> =
            Context(Syntax.NOOP, Status.OK, request)
        return ActionContextList(invokable.syntax.map {
            createContextFromSyntax(
                it,
                request,
                if (request.args.size > i) request.args[i++] else "",
                itemService,
                mobService,
                playerService,
                skills,
                recipes,
                previous
            ).let { context ->
                previous = context
                context
            }
        } as MutableList<Context<Any>>)
    }
}
