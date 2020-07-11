package kotlinmud.affect.type

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.mob.dao.MobDAO

interface Affect {
    val type: AffectType
    fun messageFromInstantiation(mob: MobDAO, target: Noun?): Message
    fun messageFromWearOff(target: Noun): Message
    fun createInstance(timeout: Int): AffectDAO
}
