package kotlinmud.generator

import kotlinmud.mob.model.MobBuilder

class MobService {
    private val mobs = mutableListOf<MobBuilder>()
    private var id = 0

    fun add(mobBuilder: MobBuilder): MobBuilder {
        val mob = MobBuilder(mobBuilder.build())
        id++
        mob.id(id)
        mobs.add(mob)
        return mob
    }
}
