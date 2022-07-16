package kotlinmud.action.service

import kotlinmud.action.helper.costApply
import kotlinmud.action.model.Action
import kotlinmud.action.model.ActionContextList
import kotlinmud.action.model.Context
import kotlinmud.action.type.Command
import kotlinmud.action.type.Status
import kotlinmud.helper.logger
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.messageToActionCreator
import kotlinmud.io.model.Response
import kotlinmud.io.model.createResponseWithEmptyActionContext
import kotlinmud.io.service.RequestService
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.RequiresDisposition
import kotlinmud.mob.type.Role

class ActionService(
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
                syntax.contains(Syntax.SUBMITTABLE_QUEST) ||
                syntax.contains(Syntax.OPTIONAL_TARGET)
        }

        private fun subCommandMatches(syntax: Syntax?, subCommand: String, input: String): Boolean {
            return syntax != Syntax.SUBCOMMAND || subCommand.startsWith(input)
        }

        private fun modifierMatches(syntax: Syntax?, modifier: String, input: String): Boolean {
            return syntax != Syntax.MODIFIER || modifier.startsWith(input)
        }
    }

    private val logger = logger(this)

    suspend fun run(request: RequestService): Response {
        if (request.input == "") {
            return createResponseWithEmptyActionContext(messageToActionCreator(""))
        }
        return runAction(request)
            ?: createResponseWithEmptyActionContext(
                messageToActionCreator("what was that?"),
                IOStatus.ERROR
            )
    }

    private suspend fun runAction(request: RequestService): Response? {
        val action = actions.find {
            commandMatches(it.command, request.getCommand()) &&
                argumentLengthMatches(it.syntax, request.args) &&
                subCommandMatches(
                    it.syntax.getOrNull(1),
                    it.getSubPart(),
                    request.getSubject(),
                ) &&
                modifierMatches(
                    it.syntax.getOrNull(2),
                    it.getModifier(),
                    request.getModifier(),
                )
        } ?: return null

        val contextList = buildActionContextList(request, action)

        return roleCheck(request, action)
            ?: dispositionCheck(request, action)
            ?: checkForBadContext(contextList)
            ?: costApply(request.mob, action)
            ?: skillCheck(request, action)
            ?: callAction(request, action, contextList)
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

    private fun skillCheck(request: RequestService, action: Action): Response? {
        action.skill?.let {
            val roll = dice(3, Math.max(0, (request.mob.skills[it.type] ?: 0) / 3))
            if (roll > 20) {
                return null
            }

            return createResponseWithEmptyActionContext(
                messageToActionCreator("You lost your concentration."),
                IOStatus.FAILED,
            )
        }

        return null
    }

    private suspend fun callAction(request: RequestService, action: Action, list: ActionContextList): Response {
        val service = actionContextBuilder(request, list)
        return checkForBadContext(list) ?: with(action.invoke(service)) {
            if (action.intent == Intent.OFFENSIVE) {
                service.createFight(list.getResultBySyntax(Syntax.OPTIONAL_TARGET))
            }
            if (action.isChained())
                run(createChainToRequest(request.mob, action))
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
            action.chainTo.toString().lowercase(),
        )
    }

    private fun buildActionContextList(request: RequestService, action: Action): ActionContextList {
        logger.debug("${request.mob} building action context :: {}, {}", action.command, action.syntax)
        var successful = true
        val contexts = mutableListOf<Context<out Any>>()
        action.argumentOrder.forEach {
            if (successful) {
                val context = contextBuilderService.createContext(
                    action.syntax[it],
                    request,
                    action,
                    if (request.args.size > it) request.args[it] else ""
                )
                contexts.add(context)
                successful = context.status == Status.OK
            }
        }
        return ActionContextList(contexts)
    }
}
