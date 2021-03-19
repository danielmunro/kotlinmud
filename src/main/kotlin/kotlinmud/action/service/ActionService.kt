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
import kotlinmud.io.model.Response
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.service.RequestService
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.RequiresDisposition
import kotlinmud.mob.type.Role

class ActionService(
    private val mobService: MobService,
    private val contextBuilderService: ContextBuilderService,
    private val actionContextBuilder: (request: RequestService, actionContextList: ActionContextList) -> ActionContextService,
    private val actions: List<Action>
) {
    companion object {
        private fun commandMatches(command: Command, input: String): Boolean {
            return command.value.startsWith(input)
        }

        private fun argumentLengthMatches(syntax: List<Syntax>, arguments: List<String>): Boolean {
            return syntax.size == arguments.size ||
                syntax.contains(Syntax.FREE_FORM) ||
                syntax.contains(Syntax.SUBMITTABLE_QUEST)
        }

        private fun subCommandMatches(syntax: Syntax, subCommand: String, input: String): Boolean {
            return syntax != Syntax.SUBCOMMAND || subCommand.startsWith(input)
        }
    }

    private val skills = createSkillList()
    private val logger = logger(this)

    suspend fun run(request: RequestService): Response {
        if (request.input == "") {
            return createResponseWithEmptyActionContext(messageToActionCreator(""))
        }
        return (if (request.input.length > 1) runSkill(request) else null)
            ?: runAction(request)
            ?: createResponseWithEmptyActionContext(
                messageToActionCreator("what was that?"),
                IOStatus.ERROR
            )
    }

    private suspend fun runSkill(request: RequestService): Response? {
        val skill = (
            skills.find {
                it is SkillAction && it.matchesRequest(request)
            } ?: return null
            ) as SkillAction

        val response = executeSkill(request, skill)

        if (skill.intent == Intent.OFFENSIVE) {
            triggerFightForOffensiveSkills(
                request.mob,
                response.actionContextList.getResultBySyntax(Syntax.TARGET_MOB)
            )
        }
        return response
    }

    private suspend fun runAction(request: RequestService): Response? {
        val action = actions.find {
            val syntax = if (it.syntax.size > 1) it.syntax[1] else Syntax.NOOP
            commandMatches(it.command, request.getCommand()) &&
                argumentLengthMatches(it.syntax, request.args) &&
                subCommandMatches(syntax, it.getSubPart(), request.getSubject())
        } ?: return null

        val contextList = buildActionContextList(request, action)

        return roleCheck(request, action)
            ?: dispositionCheck(request, action)
            ?: checkForBadContext(contextList)
            ?: costApply(request.mob, action)
            ?: callInvokable(request, action, contextList)
    }

    private suspend fun executeSkill(request: RequestService, skill: SkillAction): Response {
        val context = buildActionContextList(request, skill)
        return dispositionCheck(request, skill)
            ?: costApply(request.mob, skill)
            ?: skillRoll(request.mob.skills[skill.type] ?: error("no skill"), context)
            ?: callInvokable(request, skill, context)
    }

    private fun triggerFightForOffensiveSkills(mob: Mob, target: Mob) {
        mobService.getMobFight(mob) ?: mobService.addFight(mob, target)
    }

    private fun skillRoll(level: Int, context: ActionContextList): Response? {
        return if (percentRoll() < level) null else Response(
            IOStatus.FAILED,
            context,
            messageToActionCreator("You lost your concentration.")
        )
    }

    private fun roleCheck(request: RequestService, action: Action): Response? {
        val mobRole = request.mob.role
        val passed = when (action.minimumRole) {
            Role.Player -> true
            Role.Immortal -> mobRole != Role.Player
            Role.Admin -> mobRole == Role.Admin || mobRole == Role.Implementor
            Role.Implementor -> mobRole == Role.Implementor
        }
        return if (passed) null else createResponseWithEmptyActionContext(
            messageToActionCreator("you lack the privileges to do that.")
        )
    }

    private fun dispositionCheck(request: RequestService, requiresDisposition: RequiresDisposition): Response? {
        return if (!requiresDisposition.dispositions.contains(request.getDisposition()))
            createResponseWithEmptyActionContext(
                messageToActionCreator("you are ${request.getDisposition().value} and cannot do that.")
            )
        else
            null
    }

    private suspend fun callInvokable(request: RequestService, invokable: Invokable, list: ActionContextList): Response {
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

    private fun createChainToRequest(mob: PlayerMob, action: Action): RequestService {
        return RequestService(
            mob,
            action.chainTo.toString()
        )
    }

    private fun buildActionContextList(request: RequestService, invokable: Invokable): ActionContextList {
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
