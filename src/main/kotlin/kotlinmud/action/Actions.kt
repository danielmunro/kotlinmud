package kotlinmud.action

import kotlinmud.action.impl.createAffectsAction
import kotlinmud.action.impl.createAttributesAction
import kotlinmud.action.impl.createBuyAction
import kotlinmud.action.impl.createCloseAction
import kotlinmud.action.impl.createDownAction
import kotlinmud.action.impl.createDrinkAction
import kotlinmud.action.impl.createDropAction
import kotlinmud.action.impl.createEastAction
import kotlinmud.action.impl.createEatAction
import kotlinmud.action.impl.createEquipmentAction
import kotlinmud.action.impl.createExitsAction
import kotlinmud.action.impl.createFleeAction
import kotlinmud.action.impl.createGetAction
import kotlinmud.action.impl.createGossipAction
import kotlinmud.action.impl.createInventoryAction
import kotlinmud.action.impl.createKillAction
import kotlinmud.action.impl.createListAction
import kotlinmud.action.impl.createLookAction
import kotlinmud.action.impl.createLookAtAction
import kotlinmud.action.impl.createNorthAction
import kotlinmud.action.impl.createOpenAction
import kotlinmud.action.impl.createPracticeAction
import kotlinmud.action.impl.createRecallAction
import kotlinmud.action.impl.createRemoveAction
import kotlinmud.action.impl.createSayAction
import kotlinmud.action.impl.createScoreAction
import kotlinmud.action.impl.createSellAction
import kotlinmud.action.impl.createSitAction
import kotlinmud.action.impl.createSleepAction
import kotlinmud.action.impl.createSouthAction
import kotlinmud.action.impl.createTellAction
import kotlinmud.action.impl.createTrainAction
import kotlinmud.action.impl.createUpAction
import kotlinmud.action.impl.createWakeAction
import kotlinmud.action.impl.createWearAction
import kotlinmud.action.impl.createWeatherAction
import kotlinmud.action.impl.createWestAction
import kotlinmud.action.impl.createWhoAction

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
        createRecallAction()
    )
}
