package kotlinmud.action.service

import kotlinmud.action.helper.costApply
import kotlinmud.action.model.Action
import kotlinmud.action.model.ActionContextList
import kotlinmud.action.model.Context
import kotlinmud.action.type.Command
import kotlinmud.action.type.Invokable
import kotlinmud.action.type.Status
import kotlinmud.helper.logger
import kotlinmud.helper.math.percentRoll
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.RequiresDisposition
import org.jetbrains.exposed.sql.transactions.transaction

class ActionService(
    private val mobService: MobService,
    private val contextBuilderService: ContextBuilderService,
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
    private val logger = logger(this)

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
        val action = actions.find {
            val syntax = if (it.syntax.size > 1) it.syntax[1] else Syntax.NOOP
            commandMatches(it.command, request.getCommand()) &&
                    argumentLengthMatches(it.syntax, request.args) &&
                    subCommandMatches(syntax, it.getSubPart(), request.getSubject())
        } ?: return null

        val contextList = buildActionContextList(request, action)

        return dispositionCheck(request, action)
            ?: checkForBadContext(contextList)
            ?: costApply(request.mob, action)
            ?: callInvokable(request, action, contextList)
    }

    private fun executeSkill(request: Request, skill: SkillAction): Response {
        return dispositionCheck(request, skill)
            ?: costApply(request.mob, skill)
            ?: skillRoll(transaction { request.mob.skills.find { it.type == skill.type }?.level } ?: error("no skill"))
            ?: callInvokable(request, skill, buildActionContextList(request, skill))
    }

    private fun triggerFightForOffensiveSkills(mob: MobDAO, target: MobDAO) {
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

    private fun createChainToRequest(mob: MobDAO, action: Action): Request {
        return Request(
            mob,
            action.chainTo.toString(),
            transaction { mob.room }
        )
    }

    private fun buildActionContextList(request: Request, invokable: Invokable): ActionContextList {
        logger.debug("${request.mob} building action context :: {}, {}", invokable.command, invokable.syntax)
        var successful = true
        val contexts = mutableListOf<Context<out Any>>()
        invokable.argumentOrder.forEach {
            if (successful) {
                val context = contextBuilderService.createContext(
                    invokable.syntax[it],
                    request,
                    if (request.args.size > it) request.args[it] else ""
                )
                contexts.add(context)
                successful = context.status == Status.OK
            }
        }
        return ActionContextList(contexts)
    }
}
