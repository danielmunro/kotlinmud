package kotlinmud.action

import kotlinmud.action.impl.createBuyAction
import kotlinmud.action.impl.createCloseAction
import kotlinmud.action.impl.createDownAction
import kotlinmud.action.impl.createDrinkAction
import kotlinmud.action.impl.createDropAction
import kotlinmud.action.impl.createEastAction
import kotlinmud.action.impl.createEatAction
import kotlinmud.action.impl.createFleeAction
import kotlinmud.action.impl.createGetAction
import kotlinmud.action.impl.createHealAction
import kotlinmud.action.impl.createHealListAction
import kotlinmud.action.impl.createKillAction
import kotlinmud.action.impl.createListAction
import kotlinmud.action.impl.createNorthAction
import kotlinmud.action.impl.createOpenAction
import kotlinmud.action.impl.createPracticeAction
import kotlinmud.action.impl.createRecallAction
import kotlinmud.action.impl.createRemoveAction
import kotlinmud.action.impl.createSaveWorldAction
import kotlinmud.action.impl.createSellAction
import kotlinmud.action.impl.createSitAction
import kotlinmud.action.impl.createSleepAction
import kotlinmud.action.impl.createSouthAction
import kotlinmud.action.impl.createTrainAction
import kotlinmud.action.impl.createUpAction
import kotlinmud.action.impl.createWakeAction
import kotlinmud.action.impl.createWearAction
import kotlinmud.action.impl.createWestAction
import kotlinmud.action.impl.info.createAffectsAction
import kotlinmud.action.impl.info.createEquipmentAction
import kotlinmud.action.impl.info.createExitsAction
import kotlinmud.action.impl.info.createInventoryAction
import kotlinmud.action.impl.info.createLookAction
import kotlinmud.action.impl.info.createLookAtAction
import kotlinmud.action.impl.info.createScoreAction
import kotlinmud.action.impl.info.createWeatherAction
import kotlinmud.action.impl.info.createWhoAction
import kotlinmud.action.impl.room.creation.createRoomBuildAction
import kotlinmud.action.impl.room.creation.createRoomDescriptionAction
import kotlinmud.action.impl.room.creation.createRoomInfoAction
import kotlinmud.action.impl.room.creation.createRoomNewAction
import kotlinmud.action.impl.room.owner.createOwnerInfoAction
import kotlinmud.action.impl.room.owner.createOwnerSetAction
import kotlinmud.action.impl.social.createAttributesAction
import kotlinmud.action.impl.social.createGossipAction
import kotlinmud.action.impl.social.createSayAction
import kotlinmud.action.impl.social.createTellAction
import kotlinmud.fs.saver.WorldSaver

fun createActionsList(worldSaver: WorldSaver): List<Action> {
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
        createSaveWorldAction(worldSaver),
        createRoomNewAction(),
        createRoomBuildAction(),
        createRoomDescriptionAction(),
        createRoomInfoAction()
    )
}
