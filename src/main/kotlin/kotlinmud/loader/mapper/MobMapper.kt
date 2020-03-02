package kotlinmud.loader.mapper

import kotlinmud.item.Inventory
import kotlinmud.loader.model.MobModel
import kotlinmud.mob.Mob

class MobMapper(private val mobs: List<MobModel>) {
    fun map(): List<Mob> {
        return mobs.map {
            Mob(
                it.id,
                it.name,
                it.brief,
                it.description,
                it.disposition,
                it.hp,
                it.mana,
                it.mv,
                it.level,
                it.race,
                it.specialization,
                it.attributes.copy(),
                Inventory(),
                Inventory()
            )
        }
    }
}
