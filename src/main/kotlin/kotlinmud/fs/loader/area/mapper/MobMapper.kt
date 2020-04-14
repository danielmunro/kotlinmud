package kotlinmud.fs.loader.area.mapper

import kotlinmud.mob.Mob
import kotlinmud.mob.MobBuilder

class MobMapper(private val mobs: List<MobBuilder>) {
    fun map(): List<Mob> {
        return mobs.map {
            it.build()
        }
    }
}
