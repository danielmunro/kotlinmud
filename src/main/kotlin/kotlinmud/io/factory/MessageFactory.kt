package kotlinmud.io.factory

import kotlinmud.action.impl.player.getImprove
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.item.model.Item
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.room.dao.DoorDAO
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.type.Direction

fun messageToActionCreator(message: String): Message {
    return MessageBuilder()
        .toActionCreator(message)
        .build()
}

fun createLeaveMessage(mob: Mob, direction: Direction): Message {
    return MessageBuilder()
        .toObservers("${mob.name} leaves heading ${direction.value}.")
        .sendPrompt(false)
        .build()
}

fun createArriveMessage(mob: Mob): Message {
    return MessageBuilder()
        .toObservers("${mob.name} arrives.")
        .sendPrompt(false)
        .build()
}

fun createSingleHitMessage(attacker: Mob, defender: Mob, verb: String, verbPlural: String): Message {
    return MessageBuilder()
        .toActionCreator("you $verb $defender.")
        .toTarget("$attacker $verbPlural you.")
        .toObservers("$attacker $verbPlural $defender.")
        .sendPrompt(false)
        .build()
}

fun createDeathMessage(mob: Mob): Message {
    return MessageBuilder()
        .toActionCreator("you are DEAD!")
        .toObservers("$mob has died!")
        .sendPrompt(false)
        .build()
}

fun createFleeMessage(mob: Mob, direction: Direction): Message {
    return MessageBuilder()
        .toActionCreator("you flee!")
        .toTarget("$mob flees heading ${direction.value}!")
        .build()
}

fun createPutMessage(mob: Mob, item: Item, container: Item): Message {
    return MessageBuilder()
        .toActionCreator("you put $item into $container.")
        .toObservers("$mob puts $item into $container.")
        .build()
}

fun createRecipesMessage(recipes: List<Recipe>): Message {
    return messageToActionCreator(
        recipes.fold("Recipes:\n") {
            acc, recipe ->
            acc + "\n${recipe.name}"
        }
    )
}

fun createRecipeOfMessage(recipe: Recipe): Message {
    return messageToActionCreator(
        recipe.getComponents().entries.fold("Recipe for ${recipe.name}:\n") { acc, entry ->
            acc + "(${entry.value}) ${entry.key.toString().toLowerCase()}"
        }
    )
}

fun createGetMessage(mob: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you pick up ${item.name}.")
        .toObservers("$mob picks up ${item.name}.")
        .build()
}

fun createGetFromContainerMessage(mob: Mob, inventory: Item, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you get $item from $inventory.")
        .toObservers("$mob gets $item from $inventory.")
        .build()
}

fun createWakeMessage(mob: Mob): Message {
    return MessageBuilder()
        .toActionCreator("you stand up.")
        .toObservers("$mob stands up.")
        .build()
}

fun createTrainMessage(mob: Mob, attribute: Attribute): Message {
    return MessageBuilder()
        .toActionCreator("you train your ${getImprove(attribute)}.")
        .toObservers("$mob trains their ${getImprove(attribute)}.")
        .build()
}

fun createSleepMessage(mob: Mob): Message {
    return MessageBuilder()
        .toActionCreator("you lay down and go to sleep.")
        .toObservers("$mob lays down and goes to sleep.")
        .build()
}

fun createSitMessage(mob: Mob): Message {
    return MessageBuilder()
        .toActionCreator("you sit down.")
        .toObservers("$mob sits down.")
        .build()
}

fun createRemoveMessage(mob: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you remove $item and put it in your inventory.")
        .toObservers("$mob removes $item and puts it in their inventory.")
        .build()
}

fun createPracticeMessage(mob: Mob, skillType: SkillType): Message {
    val label = skillType.toString().toLowerCase()
    return MessageBuilder()
        .toActionCreator("you practice $label.")
        .toObservers("$mob practices $label.")
        .build()
}

fun createOpenMessage(mob: Mob, door: DoorDAO): Message {
    return MessageBuilder()
        .toActionCreator("you open $door.")
        .toObservers("$mob opens $door.")
        .build()
}

fun createKillMessage(mob: Mob, target: Mob): Message {
    return MessageBuilder()
        .toActionCreator("you scream and attack $target!")
        .toTarget("$mob screams and attacks you!")
        .toObservers("$mob screams and attacks $target!")
        .build()
}

fun createHarvestMessage(mob: Mob, resource: ResourceDAO): Message {
    return MessageBuilder()
        .toActionCreator("you successfully harvest ${resource.type.value}.")
        .toObservers("$mob harvests ${resource.type.value}.")
        .build()
}

fun createEatMessage(mob: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you eat $item.")
        .toObservers("$mob eats $item.")
        .build()
}

fun createDropMessage(mob: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you drop $item.")
        .toObservers("$mob drops $item.")
        .build()
}

fun createDrinkMessage(mob: Mob, item: Item): Message {
    val empty = if (item.quantity == 0) " $item is now empty." else ""
    return MessageBuilder()
        .toActionCreator("you drink ${item.drink.toString().toLowerCase()} from $item.$empty")
        .toObservers("$mob drinks ${item.drink.toString().toLowerCase()} from $item.")
        .build()
}

fun createCraftMessage(mob: Mob, recipe: Recipe): Message {
    return MessageBuilder()
        .toActionCreator("you craft ${recipe.name}.")
        .toObservers("$mob crafts ${recipe.name}.")
        .build()
}

fun createCloseMessage(mob: Mob, door: DoorDAO): Message {
    return MessageBuilder()
        .toActionCreator("you close $door.")
        .toObservers("$mob closes $door.")
        .build()
}

fun createBuyMessage(mob: Mob, shopkeeper: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you buy $item from $shopkeeper for ${item.worth} gold.")
        .toTarget("$mob buys $item from you.")
        .toObservers("$mob buys $item from $shopkeeper.")
        .build()
}

fun createSayMessage(mob: Mob, text: String): Message {
    return MessageBuilder()
        .toTarget("$mob says, \"$text\"")
        .build()
}

fun createTellMessage(mob: Mob, text: String): Message {
    return MessageBuilder()
        .toTarget("$mob tells you, \"$text\"")
        .build()
}

fun createGossipMessage(mob: Mob, text: String): Message {
    return MessageBuilder()
        .toTarget("$mob gossips, \"$text\"")
        .build()
}

fun createAttributesMessage(mob: Mob): Message {
    val message =
        "Your attributes are:\nStr: ${mob.base(Attribute.STR)}/${mob.calc(
            Attribute.STR
        )} Int: ${mob.base(Attribute.INT)}/${mob.calc(
            Attribute.INT
        )} Wis: ${mob.base(Attribute.WIS)}/${mob.calc(
            Attribute.WIS
        )} Dex: ${mob.base(Attribute.DEX)}/${mob.calc(
            Attribute.DEX
        )} Con: ${mob.base(Attribute.CON)}/${mob.calc(
            Attribute.CON
        )}"
    return MessageBuilder()
        .toActionCreator(message)
        .toObservers(message)
        .build()
}

fun createSellMessage(mob: Mob, shopkeeper: Mob, item: Item): Message {
    return MessageBuilder()
        .toActionCreator("you sell $item to $shopkeeper for ${item.worth} gold.")
        .toTarget("$mob sells $item to you.")
        .toObservers("$mob sells $item to $shopkeeper.")
        .build()
}

fun createWearMessage(mob: Mob, item: Item, removed: Item?): Message {
    return MessageBuilder()
        .toActionCreator("you ${if (removed != null) "remove $removed and " else ""}wear $item.")
        .toObservers("$mob ${if (removed != null) "removes $removed and " else ""}wears $item.")
        .build()
}
