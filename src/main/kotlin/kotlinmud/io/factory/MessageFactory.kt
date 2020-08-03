package kotlinmud.io.factory

import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.type.Direction

fun messageToActionCreator(message: String): Message {
    return MessageBuilder()
        .toActionCreator(message)
        .build()
}

fun createLeaveMessage(mob: MobDAO, direction: Direction): Message {
    return MessageBuilder()
        .toActionCreator("you leave heading ${direction.value}.")
        .toObservers("${mob.name} leaves heading ${direction.value}.")
        .sendPrompt(false)
        .build()
}

fun createArriveMessage(mob: MobDAO): Message {
    return MessageBuilder()
        .toObservers("${mob.name} arrives.")
        .sendPrompt(false)
        .build()
}

fun createSingleHitMessage(attacker: MobDAO, defender: MobDAO, verb: String, verbPlural: String): Message {
    return MessageBuilder()
        .toActionCreator("you $verb $defender.")
        .toTarget("$attacker $verbPlural you.")
        .toObservers("$attacker $verbPlural $defender.")
        .sendPrompt(false)
        .build()
}

fun createDeathMessage(mob: MobDAO): Message {
    return MessageBuilder()
        .toActionCreator("you are DEAD!")
        .toObservers("$mob has died!")
        .sendPrompt(false)
        .build()
}

fun createFleeMessage(mob: MobDAO, direction: Direction): Message {
    return MessageBuilder()
        .toActionCreator("you flee!")
        .toTarget("$mob flees heading ${direction.value}!")
        .build()
}

fun createPutMessage(mob: MobDAO, item: ItemDAO, container: ItemDAO): Message {
    return MessageBuilder()
        .toActionCreator("you put $item into $container.")
        .toObservers("$mob puts $item into $container.")
        .build()
}
