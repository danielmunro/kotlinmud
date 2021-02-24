package kotlinmud.affect.type

import kotlinmud.affect.dao.AffectDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob

interface Affect {
    val type: AffectType
    fun messageFromInstantiation(mob: Mob, target: Noun?): Message
    fun messageFromWearOff(target: Noun): Message
    fun createInstance(timeout: Int): AffectDAO
}
