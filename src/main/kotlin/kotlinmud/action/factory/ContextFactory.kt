package kotlinmud.action.factory

import kotlinmud.action.contextBuilder.AvailableDrinkContextBuilder
import kotlinmud.action.contextBuilder.AvailableFoodContextBuilder
import kotlinmud.action.contextBuilder.AvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.AvailableNounContextBuilder
import kotlinmud.action.contextBuilder.CastContextBuilder
import kotlinmud.action.contextBuilder.CommandContextBuilder
import kotlinmud.action.contextBuilder.DirectionToExitContextBuilder
import kotlinmud.action.contextBuilder.DirectionWithNoExitContextBuilder
import kotlinmud.action.contextBuilder.DoorInRoomContextBuilder
import kotlinmud.action.contextBuilder.EquipmentInInventoryContextBuilder
import kotlinmud.action.contextBuilder.EquippedItemContextBuilder
import kotlinmud.action.contextBuilder.FreeFormContextBuilder
import kotlinmud.action.contextBuilder.ItemFromMerchantContextBuilder
import kotlinmud.action.contextBuilder.ItemInAvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInRoomContextBuilder
import kotlinmud.action.contextBuilder.ItemToSellContextBuilder
import kotlinmud.action.contextBuilder.MobInRoomContextBuilder
import kotlinmud.action.contextBuilder.OptionalFurnitureContextBuilder
import kotlinmud.action.contextBuilder.OptionalTargetContextBuilder
import kotlinmud.action.contextBuilder.PlayerMobContextBuilder
import kotlinmud.action.contextBuilder.RecipeContextBuilder
import kotlinmud.action.contextBuilder.ResourceInRoomContextBuilder
import kotlinmud.action.contextBuilder.SkillToPracticeContextBuilder
import kotlinmud.action.contextBuilder.SpellContextBuilder
import kotlinmud.action.contextBuilder.SpellFromHealerContextBuilder
import kotlinmud.action.contextBuilder.TargetMobContextBuilder
import kotlinmud.action.contextBuilder.TrainableContextBuilder
import kotlinmud.action.model.Context
import kotlinmud.action.type.Status
import kotlinmud.io.model.Request
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Recipe
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.Skill
import kotlinmud.player.service.PlayerService

fun createContextFromSyntax(
    syntax: Syntax,
    request: Request,
    word: String,
    itemService: ItemService,
    mobService: MobService,
    playerService: PlayerService,
    skills: List<Skill>,
    recipes: List<Recipe>,
    previous: Context<Any>
): Context<Any> {
    return when (syntax) {
        Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.room).build(syntax, word)
        Syntax.DIRECTION_WITH_NO_EXIT -> DirectionWithNoExitContextBuilder(request.room).build(syntax, word)
        Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
        Syntax.SUBCOMMAND -> CommandContextBuilder().build(syntax, word)
        Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(itemService, request.mob).build(syntax, word)
        Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(itemService, request.room).build(syntax, word)
        Syntax.EQUIPMENT_IN_INVENTORY -> EquipmentInInventoryContextBuilder(itemService, request.mob).build(syntax, word)
        Syntax.EQUIPPED_ITEM -> EquippedItemContextBuilder(request.mob).build(syntax, word)
        Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
        Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, itemService, request.mob, request.room).build(syntax, word)
        Syntax.TARGET_MOB -> TargetMobContextBuilder(mobService, request.mob, request.room).build(syntax, word)
        Syntax.OPTIONAL_TARGET -> OptionalTargetContextBuilder(request.mob, mobService.getMobsForRoom(request.room) + itemService.findAllByOwner(request.mob)).build(syntax, word)
        Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.room).build(syntax, word)
        Syntax.FREE_FORM -> FreeFormContextBuilder(request.args).build(syntax, word)
        Syntax.ITEM_FROM_MERCHANT -> ItemFromMerchantContextBuilder(itemService, request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
        Syntax.ITEM_TO_SELL -> ItemToSellContextBuilder(itemService, request.mob, mobService.getMobsForRoom(request.room)).build(syntax, word)
        Syntax.SPELL -> SpellContextBuilder(skills).build(syntax, word)
        Syntax.SPELL_FROM_HEALER -> SpellFromHealerContextBuilder(mobService.getMobsForRoom(request.room)).build(syntax, word)
        Syntax.CAST -> CastContextBuilder().build(syntax, word)
        Syntax.PLAYER_MOB -> PlayerMobContextBuilder(mobService).build(syntax, word)
        Syntax.AVAILABLE_DRINK -> AvailableDrinkContextBuilder(itemService, request.mob, request.room).build(syntax, word)
        Syntax.AVAILABLE_FOOD -> AvailableFoodContextBuilder(itemService, request.mob).build(syntax, word)
        Syntax.TRAINABLE -> TrainableContextBuilder(mobService, playerService, request.mob).build(syntax, word)
        Syntax.SKILL_TO_PRACTICE -> SkillToPracticeContextBuilder(playerService, request.mob).build(syntax, word)
        Syntax.RECIPE -> RecipeContextBuilder(recipes).build(syntax, word)
        Syntax.RESOURCE_IN_ROOM -> ResourceInRoomContextBuilder(request.room).build(syntax, word)
        Syntax.AVAILABLE_ITEM_INVENTORY -> AvailableItemInventoryContextBuilder(request.mob, request.room, itemService).build(syntax, word)
        Syntax.ITEM_IN_AVAILABLE_INVENTORY -> ItemInAvailableItemInventoryContextBuilder(itemService, previous.result as HasInventory).build(syntax, word)
        Syntax.OPTIONAL_FURNITURE -> OptionalFurnitureContextBuilder(itemService.findAllByOwner(request.room)).build(syntax, word)
        Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
    }
}
