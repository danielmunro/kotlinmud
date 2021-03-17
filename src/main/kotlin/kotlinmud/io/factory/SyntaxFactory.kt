package kotlinmud.io.factory

import kotlinmud.io.type.Syntax

fun command(): List<Syntax> {
    return listOf(Syntax.COMMAND)
}

fun availableNoun(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.AVAILABLE_NOUN)
}

fun freeForm(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.FREE_FORM)
}

fun drink(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.AVAILABLE_DRINK)
}

fun recipe(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.RECIPE)
}

fun subcommandWithRecipe(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.RECIPE)
}

fun itemFromMerchant(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_FROM_MERCHANT)
}

fun itemToSell(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_TO_SELL)
}

fun itemInInventory(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY)
}

fun availableInventoryAndItem(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_IN_INVENTORY, Syntax.AVAILABLE_ITEM_INVENTORY)
}

fun itemInInventoryAndAvailableInventory(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_IN_AVAILABLE_INVENTORY, Syntax.AVAILABLE_ITEM_INVENTORY)
}

fun itemInRoom(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.ITEM_IN_ROOM)
}

fun resourceInRoom(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.RESOURCE_IN_ROOM)
}

fun resource(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.RESOURCE_IN_ROOM)
}

fun foodInInventory(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.AVAILABLE_FOOD)
}

fun spell(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SPELL, Syntax.OPTIONAL_TARGET)
}

fun offensiveSpell(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SPELL, Syntax.TARGET_MOB)
}

fun target(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.TARGET_MOB)
}

fun spellFromHealer(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SPELL_FROM_HEALER)
}

fun doorInRoom(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.DOOR_IN_ROOM)
}

fun mobInRoom(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.MOB_IN_ROOM)
}

fun directionToExit(): List<Syntax> {
    return listOf(Syntax.DIRECTION_TO_EXIT)
}

fun skillToPractice(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SKILL_TO_PRACTICE)
}

fun equippedItem(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.EQUIPPED_ITEM)
}

fun trainable(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.TRAINABLE)
}

fun equipmentInInventory(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.EQUIPMENT_IN_INVENTORY)
}

fun playerFreeForm(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.PLAYER_MOB, Syntax.FREE_FORM)
}

fun playerMob(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.PLAYER_MOB)
}

fun optionalFurniture(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.OPTIONAL_FURNITURE)
}

fun subcommandDirectionNoExit(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.DIRECTION_WITH_NO_EXIT)
}

fun subcommand(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND)
}

fun subcommandWithModifier(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.FREE_FORM)
}

fun subcommandPlayerMob(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.PLAYER_MOB)
}
