package kotlinmud.affect

import kotlinmud.io.Message
import kotlinmud.mob.Mob

interface Affect {
    val type: AffectType
    fun messageFromInstantiation(mob: Mob, target: Mob?): Message
    fun messageFromWearOff(mob: Mob): Message
    fun createInstance(timeout: Int): AffectInstance
}
