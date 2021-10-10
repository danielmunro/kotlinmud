package kotlinmud.startup.validator

import kotlinmud.startup.model.ItemModel
import kotlinmud.startup.model.MobModel
import kotlinmud.startup.model.QuestModel
import kotlinmud.startup.model.RoomModel
import kotlinmud.startup.validator.helper.validateUniqueIds

class ModelCollectionValidator(
    private val rooms: List<RoomModel>,
    private val mobs: List<MobModel>,
    private val items: List<ItemModel>,
    private val quests: List<QuestModel>,
) : Validator {
    override fun validate() {
        validateUniqueIds(rooms)
        validateUniqueIds(mobs)
        validateUniqueIds(items)
        validateUniqueIds(quests)
    }
}
