package kotlinmud.action.impl

import kotlinmud.action.Action
import kotlinmud.action.Command
import kotlinmud.action.mustBeFighting
import kotlinmud.io.Message
import kotlinmud.io.command
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType

fun createFleeAction(): Action {
    return Action(Command.FLEE, mustBeFighting(), command(), listOf(Cost(CostType.MV_PERCENT, 25)), Command.LOOK) {
        it.endFight()
        val exit = it.getExits().random()
        it.moveMob(
            exit.destination,
            exit.direction)
        it.createResponse(
            Message(
                "you flee!",
                "${it.getMob()} flees!")
        )
    }
}
