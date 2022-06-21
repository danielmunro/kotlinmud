package kotlinmud.action.service

import kotlinmud.action.contextBuilder.AcceptedQuestContextBuilder
import kotlinmud.action.contextBuilder.AvailableDrinkContextBuilder
import kotlinmud.action.contextBuilder.AvailableFoodContextBuilder
import kotlinmud.action.contextBuilder.AvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.AvailableNounContextBuilder
import kotlinmud.action.contextBuilder.AvailablePotionContextBuilder
import kotlinmud.action.contextBuilder.AvailableQuestContextBuilder
import kotlinmud.action.contextBuilder.CommandContextBuilder
import kotlinmud.action.contextBuilder.DirectionToExitContextBuilder
import kotlinmud.action.contextBuilder.DirectionWithNoExitContextBuilder
import kotlinmud.action.contextBuilder.DoorInRoomContextBuilder
import kotlinmud.action.contextBuilder.EquipmentInInventoryContextBuilder
import kotlinmud.action.contextBuilder.EquippedItemContextBuilder
import kotlinmud.action.contextBuilder.FreeFormContextBuilder
import kotlinmud.action.contextBuilder.IntegerValueContextBuilder
import kotlinmud.action.contextBuilder.ItemFromMerchantContextBuilder
import kotlinmud.action.contextBuilder.ItemInAvailableItemInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInInventoryContextBuilder
import kotlinmud.action.contextBuilder.ItemInRoomContextBuilder
import kotlinmud.action.contextBuilder.ItemToSellContextBuilder
import kotlinmud.action.contextBuilder.MobInRoomContextBuilder
import kotlinmud.action.contextBuilder.NoopContextBuilder
import kotlinmud.action.contextBuilder.OptionalFurnitureContextBuilder
import kotlinmud.action.contextBuilder.OptionalTargetContextBuilder
import kotlinmud.action.contextBuilder.PlayerMobContextBuilder
import kotlinmud.action.contextBuilder.RecipeContextBuilder
import kotlinmud.action.contextBuilder.ResourceInRoomContextBuilder
import kotlinmud.action.contextBuilder.SkillToPracticeContextBuilder
import kotlinmud.action.contextBuilder.SpellContextBuilder
import kotlinmud.action.contextBuilder.SpellFromHealerContextBuilder
import kotlinmud.action.contextBuilder.SubmittableQuestContextBuilder
import kotlinmud.action.contextBuilder.TrainableContextBuilder
import kotlinmud.action.model.Action
import kotlinmud.action.model.Context
import kotlinmud.io.service.RequestService
import kotlinmud.io.type.Syntax
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Recipe
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.Skill
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService

class ContextBuilderService(
    private val itemService: ItemService,
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val questService: QuestService,
    private val skills: List<Skill>,
    private val recipes: List<Recipe>
) {
    private var previous: Context<out Any>? = null

    fun createContext(syntax: Syntax, request: RequestService, action: Action, word: String): Context<out Any> {
        val contextBuilder = when (syntax) {
            Syntax.DIRECTION_TO_EXIT -> DirectionToExitContextBuilder(request.getRoom())
            Syntax.DIRECTION_WITH_NO_EXIT -> DirectionWithNoExitContextBuilder(request.getRoom())
            Syntax.COMMAND -> CommandContextBuilder()
            Syntax.SUBCOMMAND -> CommandContextBuilder()
            Syntax.MODIFIER -> CommandContextBuilder()
            Syntax.VALUE -> IntegerValueContextBuilder()
            Syntax.ITEM_IN_INVENTORY -> ItemInInventoryContextBuilder(request.mob)
            Syntax.ITEM_IN_ROOM -> ItemInRoomContextBuilder(request.getRoom())
            Syntax.EQUIPMENT_IN_INVENTORY -> EquipmentInInventoryContextBuilder(request.mob)
            Syntax.EQUIPPED_ITEM -> EquippedItemContextBuilder(request.mob)
            Syntax.MOB_IN_ROOM -> MobInRoomContextBuilder(mobService.findMobsInRoom(request.getRoom()))
            Syntax.AVAILABLE_NOUN -> AvailableNounContextBuilder(mobService, request.mob, request.getRoom())
            Syntax.OPTIONAL_TARGET -> OptionalTargetContextBuilder(request.mob, mobService.findMobsInRoom(request.getRoom()) + request.mob.items, action, mobService.getMobFight(request.mob))
            Syntax.DOOR_IN_ROOM -> DoorInRoomContextBuilder(request.getRoom())
            Syntax.FREE_FORM -> FreeFormContextBuilder(request.args)
            Syntax.ITEM_FROM_MERCHANT -> ItemFromMerchantContextBuilder(itemService, request.mob, mobService.findMobsInRoom(request.getRoom()))
            Syntax.ITEM_TO_SELL -> ItemToSellContextBuilder(request.mob, mobService.findMobsInRoom(request.getRoom()))
            Syntax.SPELL -> SpellContextBuilder(skills)
            Syntax.SPELL_FROM_HEALER -> SpellFromHealerContextBuilder(mobService.findMobsInRoom(request.getRoom()))
            Syntax.PLAYER_MOB -> PlayerMobContextBuilder(playerService)
            Syntax.AVAILABLE_DRINK -> AvailableDrinkContextBuilder(request.mob, request.getRoom())
            Syntax.AVAILABLE_FOOD -> AvailableFoodContextBuilder(request.mob)
            Syntax.AVAILABLE_POTION -> AvailablePotionContextBuilder(request.mob)
            Syntax.TRAINABLE -> TrainableContextBuilder(mobService, request.mob)
            Syntax.SKILL_TO_PRACTICE -> SkillToPracticeContextBuilder(request.mob)
            Syntax.RECIPE -> RecipeContextBuilder(recipes)
            Syntax.RESOURCE_IN_ROOM -> ResourceInRoomContextBuilder(request.getRoom())
            Syntax.AVAILABLE_ITEM_INVENTORY -> AvailableItemInventoryContextBuilder(request.mob, request.getRoom())
            Syntax.ITEM_IN_AVAILABLE_INVENTORY -> ItemInAvailableItemInventoryContextBuilder(previous?.result!!)
            Syntax.OPTIONAL_FURNITURE -> OptionalFurnitureContextBuilder(request.getRoom().items)
            Syntax.ACCEPTED_QUEST -> AcceptedQuestContextBuilder(questService, request.mob)
            Syntax.AVAILABLE_QUEST -> AvailableQuestContextBuilder(questService, request.mob)
            Syntax.SUBMITTABLE_QUEST -> SubmittableQuestContextBuilder(questService, request.mob)
            Syntax.NOOP -> NoopContextBuilder()
        }

        return contextBuilder.build(syntax, word).also {
            previous = it
        }
    }
}
