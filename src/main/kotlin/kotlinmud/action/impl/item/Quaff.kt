package kotlinmud.action.impl.item

import kotlinmud.action.builder.ActionBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.io.factory.potionInInventory
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.Spell

fun createQuaffAction(): Action {
    return ActionBuilder(Command.QUAFF).also {
        it.syntax = potionInInventory()
    } build { svc ->
        val item = svc.get<Item>(Syntax.AVAILABLE_POTION)
        val mob = svc.getMob()

        // consume item
        mob.items.remove(item)
        svc.destroyItem(item)

        // create message for quaff
        val messages = mutableListOf(
            MessageBuilder()
                .toActionCreator("you quaff $item.")
                .toTarget("you quaff $item.")
                .toObservers("$mob quaffs $item.")
                .build()
        )

        // cast and generate cast messages
        item.affects.forEach {
            mob.affects.add(Affect(it.type, it.timeout, it.attributes))
        }
        val skillHash = HashMap<SkillType, Spell>()
        svc.skills.map {
            if (it is Spell) {
                skillHash[it.type] = it
            }
        }
        item.spells.forEach {
            val spell = skillHash[it]!!
            spell.cast(mob, mob)
            messages.add(spell.createMessage(mob, mob))
        }

        // fold all cast messages into a single message
        val toActionCreator = messages.fold("") { acc: String, current: Message ->
            acc + current.toActionCreator + "\n"
        }.trim()
        val toTarget = messages.fold("") { acc: String, current: Message ->
            acc + current.toTarget + "\n"
        }.trim()
        val toObservers = messages.fold("") { acc: String, current: Message ->
            acc + current.toObservers + "\n"
        }.trim()

        svc.createOkResponse(
            MessageBuilder()
                .toActionCreator(toActionCreator)
                .toTarget(toTarget)
                .toObservers(toObservers)
                .build()
        )
    }
}
