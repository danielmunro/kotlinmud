package kotlinmud.affect.type

import kotlinmud.affect.model.Affect
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.mob.model.Mob

interface AffectInterface {
    val type: AffectType
    fun messageFromInstantiation(mob: Mob, target: Noun?): Message
    fun messageFromWearOff(target: Noun): Message
    fun createInstance(timeout: Int): Affect
}
