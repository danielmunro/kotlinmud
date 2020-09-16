package kotlinmud.event.observer.impl.regen

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.type.EventType
import kotlinmud.helper.math.dice
import kotlinmud.mob.skill.type.SkillType

class FastHealingObserver : Observer {
    override val eventType = EventType.REGEN

    override fun <T> processEvent(event: Event<T>) {
        with(event.subject as RegenEvent) {
            this.mob.getSkill(SkillType.FAST_HEALING)?.let {
                if (it.level > dice(1, 100)) {
                    event.subject.hpRegenRate += 0.1
                }
            }
        }
    }
}
