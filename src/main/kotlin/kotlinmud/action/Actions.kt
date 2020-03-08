package kotlinmud.action

import kotlinmud.action.impl.*

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
        createRemoveAction(),
        createWearAction(),
        createBuyAction(),
        createSellAction(),
        createListAction(),
        createScoreAction()
    )
}
