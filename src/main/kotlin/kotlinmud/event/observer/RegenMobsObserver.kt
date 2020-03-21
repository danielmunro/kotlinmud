package kotlinmud.event.observer

import kotlinmud.attributes.Attribute
import kotlinmud.event.Event
import kotlinmud.event.EventResponse
import kotlinmud.event.EventType
import kotlinmud.math.normalizeDouble
import kotlinmud.mob.Disposition
import kotlinmud.room.RegenLevel
import kotlinmud.service.MobService

class RegenMobsObserver(private val mobService: MobService) : Observer {
    override val eventTypes: List<EventType> = listOf(EventType.TICK)

    override fun <T, A> processEvent(event: Event<T>): EventResponse<A> {
        mobService.getMobRooms().filter { !it.mob.isIncapacitated() }.forEach {
            val regen = normalizeDouble(
                0.0,
                getRegenRate(it.room.regen) + getDispositionRegenRate(it.mob.disposition),
                1.0
            )
            it.mob.increaseHp((regen * it.mob.calc(Attribute.HP)).toInt())
            it.mob.increaseMana((regen * it.mob.calc(Attribute.MANA)).toInt())
            it.mob.increaseMv((regen * it.mob.calc(Attribute.MV)).toInt())
        }

        @Suppress("UNCHECKED_CAST")
        return EventResponse(event.subject as A)
    }

}

fun getRegenRate(regenLevel: RegenLevel): Double {
    return when(regenLevel) {
        RegenLevel.NONE -> 0.0
        RegenLevel.LOW -> 0.05
        RegenLevel.NORMAL -> 0.1
        RegenLevel.HIGH -> .20
        RegenLevel.FULL_HEAL -> 1.0
    }
}

fun getDispositionRegenRate(disposition: Disposition): Double {
    return when(disposition) {
        Disposition.DEAD -> 0.0
        Disposition.SLEEPING -> 0.15
        Disposition.SITTING -> 0.05
        Disposition.STANDING -> 0.0
        Disposition.FIGHTING -> -0.15
    }
}
