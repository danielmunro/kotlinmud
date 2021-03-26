package kotlinmud.action.impl.fight

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.skill.impl.Berserk
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType

fun createBerserkAction(): Action {
    return ActionBuilder(Command.BERSERK).also {
        it.costs = listOf(Cost(CostType.MV_PERCENT, 20))
        it.skill = Berserk()
    } build {
        val affect = createAffect(AffectType.BERSERK)
        val mob = it.getMob()
        mob.affects.add(affect)
        it.createOkResponse(
            MessageBuilder()
                .toActionCreator("Your pulse speeds up as you are consumed by rage!")
                .toObservers("$mob's pulse speeds up as they are consumed by rage!")
                .build(),
            2
        )
    }
}
