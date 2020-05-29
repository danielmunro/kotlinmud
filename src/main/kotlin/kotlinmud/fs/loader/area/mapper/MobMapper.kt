package kotlinmud.fs.loader.area.mapper

import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.MobBuilder

class MobMapper(private val mobs: List<MobBuilder>) {
    fun map(): List<Mob> {
        return mobs.map {
            it.build()
        }
    }
}
