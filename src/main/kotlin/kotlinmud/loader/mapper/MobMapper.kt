package kotlinmud.loader.mapper

import kotlinmud.mob.Mob

class MobMapper(private val mobs: List<Mob.Builder>) {
    fun map(): List<Mob> {
        return mobs.map {
            it.build()
        }
    }
}
