package kotlinmud.persistence.validator

import kotlinmud.persistence.model.ItemModel
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.QuestModel
import kotlinmud.persistence.model.RoomModel
import kotlinmud.persistence.validator.helper.validateUniqueIds

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
