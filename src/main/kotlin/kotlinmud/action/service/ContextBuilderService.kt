package kotlinmud.action.service

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
import kotlinmud.io.service.RequestService
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Recipe
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.Skill
import kotlinmud.player.service.PlayerService

class ContextBuilderService(
    private val itemService: ItemService,
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val skills: List<Skill>,
    private val recipes: List<Recipe>
) {
    private var previous: Context<out Any>? = null

    fun createContext(syntax: Syntax, request: RequestService, word: String): Context<out Any> {
        val context = when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.getRoom()).build(syntax, word)
            Syntax.DIRECTION_WITH_NO_EXIT -> DirectionWithNoExitContextBuilder(request.getRoom()).build(syntax, word)
            Syntax.COMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.SUBCOMMAND -> CommandContextBuilder().build(syntax, word)
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(itemService, request.getMob()).build(syntax, word)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(itemService, request.getRoom()).build(syntax, word)
            Syntax.EQUIPMENT_IN_INVENTORY -> EquipmentInInventoryContextBuilder(itemService, request.getMob()).build(syntax, word)
            Syntax.EQUIPPED_ITEM -> EquippedItemContextBuilder(request.getMob()).build(syntax, word)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(findMobsForRoom(request.getRoom())).build(syntax, word)
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(itemService, request.getMob(), request.getRoom()).build(syntax, word)
            Syntax.TARGET_MOB -> TargetMobContextBuilder(mobService, request.getMob(), request.getRoom()).build(syntax, word)
            Syntax.OPTIONAL_TARGET -> OptionalTargetContextBuilder(request.getMob(), findMobsForRoom(request.getRoom()) + itemService.findAllByOwner(request.getMob())).build(syntax, word)
            Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.getRoom()).build(syntax, word)
            Syntax.FREE_FORM -> FreeFormContextBuilder(request.args).build(syntax, word)
            Syntax.ITEM_FROM_MERCHANT -> ItemFromMerchantContextBuilder(itemService, request.getMob(), findMobsForRoom(request.getRoom())).build(syntax, word)
            Syntax.ITEM_TO_SELL -> ItemToSellContextBuilder(itemService, request.getMob(), findMobsForRoom(request.getRoom())).build(syntax, word)
            Syntax.SPELL -> SpellContextBuilder(skills).build(syntax, word)
            Syntax.SPELL_FROM_HEALER -> SpellFromHealerContextBuilder(findMobsForRoom(request.getRoom())).build(syntax, word)
            Syntax.CAST -> CastContextBuilder().build(syntax, word)
            Syntax.PLAYER_MOB -> PlayerMobContextBuilder().build(syntax, word)
            Syntax.AVAILABLE_DRINK -> AvailableDrinkContextBuilder(itemService, request.getMob(), request.getRoom()).build(syntax, word)
            Syntax.AVAILABLE_FOOD -> AvailableFoodContextBuilder(itemService, request.getMob()).build(syntax, word)
            Syntax.TRAINABLE -> TrainableContextBuilder(playerService, request.getMob()).build(syntax, word)
            Syntax.SKILL_TO_PRACTICE -> SkillToPracticeContextBuilder(playerService, request.getMob()).build(syntax, word)
            Syntax.RECIPE -> RecipeContextBuilder(recipes).build(syntax, word)
            Syntax.RESOURCE_IN_ROOM -> ResourceInRoomContextBuilder(request.getRoom()).build(syntax, word)
            Syntax.AVAILABLE_ITEM_INVENTORY -> AvailableItemInventoryContextBuilder(request.getMob(), request.getRoom(), itemService).build(syntax, word)
            Syntax.ITEM_IN_AVAILABLE_INVENTORY -> ItemInAvailableItemInventoryContextBuilder(itemService, previous?.result as HasInventory).build(syntax, word)
            Syntax.OPTIONAL_FURNITURE -> OptionalFurnitureContextBuilder(itemService.findAllByOwner(request.getRoom())).build(syntax, word)
            Syntax.NOOP -> Context(syntax, Status.ERROR, "What was that?")
        }
        previous = context
        return context
    }
}
