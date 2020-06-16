package kotlinmud.generator

import kotlinmud.mob.model.Mob

class MobService {
    private val mobs = mutableListOf<Mob>()

    fun add(mob: Mob) {
//        mob.id = 1
        mobs.add(mob)
    }
}
