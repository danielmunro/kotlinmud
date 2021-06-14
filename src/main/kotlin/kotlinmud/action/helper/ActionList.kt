package kotlinmud.action.helper

import kotlinmud.action.impl.admin.createBanAction
import kotlinmud.action.impl.admin.createUnbanAction
import kotlinmud.action.impl.createCastAction
import kotlinmud.action.impl.createCloseAction
import kotlinmud.action.impl.createDownAction
import kotlinmud.action.impl.createEastAction
import kotlinmud.action.impl.createNorthAction
import kotlinmud.action.impl.createOpenAction
import kotlinmud.action.impl.createSouthAction
import kotlinmud.action.impl.createUpAction
import kotlinmud.action.impl.createWestAction
import kotlinmud.action.impl.disposition.createSitAction
import kotlinmud.action.impl.disposition.createSleepAction
import kotlinmud.action.impl.disposition.createWakeAction
import kotlinmud.action.impl.equipment.createRemoveAction
import kotlinmud.action.impl.equipment.createWearAction
import kotlinmud.action.impl.fight.createBackstabAction
import kotlinmud.action.impl.fight.createBashAction
import kotlinmud.action.impl.fight.createBerserkAction
import kotlinmud.action.impl.fight.createBiteAction
import kotlinmud.action.impl.fight.createDisarmAction
import kotlinmud.action.impl.fight.createFleeAction
import kotlinmud.action.impl.fight.createHamstringAction
import kotlinmud.action.impl.fight.createKillAction
import kotlinmud.action.impl.fight.createTailAction
import kotlinmud.action.impl.fight.createTripAction
import kotlinmud.action.impl.info.createAffectsAction
import kotlinmud.action.impl.info.createAttributesAction
import kotlinmud.action.impl.info.createEquipmentAction
import kotlinmud.action.impl.info.createExitsAction
import kotlinmud.action.impl.info.createInventoryAction
import kotlinmud.action.impl.info.createLookAction
import kotlinmud.action.impl.info.createLookAtAction
import kotlinmud.action.impl.info.createScanAction
import kotlinmud.action.impl.info.createScoreAction
import kotlinmud.action.impl.info.createTimeAction
import kotlinmud.action.impl.info.createWeatherAction
import kotlinmud.action.impl.info.createWhoAction
import kotlinmud.action.impl.item.createCraftAction
import kotlinmud.action.impl.item.createDropAction
import kotlinmud.action.impl.item.createGetAction
import kotlinmud.action.impl.item.createGetFromItemAction
import kotlinmud.action.impl.item.createPutAction
import kotlinmud.action.impl.item.createQuaffAction
import kotlinmud.action.impl.item.createRecipeOfAction
import kotlinmud.action.impl.item.createRecipesAction
import kotlinmud.action.impl.player.createDescriptionAction
import kotlinmud.action.impl.player.createDrinkAction
import kotlinmud.action.impl.player.createEatAction
import kotlinmud.action.impl.player.createHealAction
import kotlinmud.action.impl.player.createHealListAction
import kotlinmud.action.impl.player.createLevelAction
import kotlinmud.action.impl.player.createPracticeAction
import kotlinmud.action.impl.player.createRecallAction
import kotlinmud.action.impl.player.createTrainAction
import kotlinmud.action.impl.quest.createQuestAbandonAction
import kotlinmud.action.impl.quest.createQuestAcceptAction
import kotlinmud.action.impl.quest.createQuestListAction
import kotlinmud.action.impl.quest.createQuestLogAction
import kotlinmud.action.impl.quest.createQuestSubmitAction
import kotlinmud.action.impl.resource.createHarvestAction
import kotlinmud.action.impl.resource.createTillAction
import kotlinmud.action.impl.room.createLockAction
import kotlinmud.action.impl.room.createOwnerInfoAction
import kotlinmud.action.impl.room.createOwnerSetAction
import kotlinmud.action.impl.room.createUnlockAction
import kotlinmud.action.impl.shop.createBuyAction
import kotlinmud.action.impl.shop.createListAction
import kotlinmud.action.impl.shop.createSellAction
import kotlinmud.action.impl.social.createGossipAction
import kotlinmud.action.impl.social.createSayAction
import kotlinmud.action.impl.social.createTellAction
import kotlinmud.action.model.Action

fun createActionsList(): List<Action> {
    return listOf(
        createLookAction(),
        createLookAtAction(),
        createNorthAction(),
        createSouthAction(),
        createEastAction(),
        createWestAction(),
        createUpAction(),
        createDownAction(),
        createGetAction(),
        createGetFromItemAction(),
        createPutAction(),
        createDropAction(),
        createInventoryAction(),
        createEquipmentAction(),
        createKillAction(),
        createFleeAction(),
        createSitAction(),
        createWakeAction(),
        createSleepAction(),
        createOpenAction(),
        createCloseAction(),
        createLockAction(),
        createUnlockAction(),
        createSayAction(),
        createGossipAction(),
        createTellAction(),
        createRemoveAction(),
        createWearAction(),
        createBuyAction(),
        createSellAction(),
        createListAction(),
        createScoreAction(),
        createDrinkAction(),
        createEatAction(),
        createTrainAction(),
        createPracticeAction(),
        createWhoAction(),
        createExitsAction(),
        createAttributesAction(),
        createWeatherAction(),
        createAffectsAction(),
        createRecallAction(),
        createHealAction(),
        createHealListAction(),
        createOwnerInfoAction(),
        createOwnerSetAction(),
        createCraftAction(),
        createHarvestAction(),
        createRecipesAction(),
        createRecipeOfAction(),
        createTillAction(),
        createQuestListAction(),
        createQuestAcceptAction(),
        createQuestAbandonAction(),
        createQuestLogAction(),
        createQuestSubmitAction(),
        createTimeAction(),
        createDescriptionAction(),
        createQuaffAction(),
        createScanAction(),
        createLevelAction(),

        // admin
        createBanAction(),
        createUnbanAction(),

        // skills
        createDisarmAction(),
        createBackstabAction(),
        createBashAction(),
        createBerserkAction(),
        createBiteAction(),
        createTailAction(),
        createTripAction(),
        createHamstringAction(),

        // spells
        createCastAction(),
    )
}
