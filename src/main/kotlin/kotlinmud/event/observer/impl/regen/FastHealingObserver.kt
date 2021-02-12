package kotlinmud.event.observer.impl.regen

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.mob.skill.type.SkillType

class FastHealingObserver : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        with(event.subject as RegenEvent) {
            this.mob.getSkill(SkillType.FAST_HEALING)?.let {
                if (it.level / 2 > dice(1, 100)) {
                    event.subject.hpRegenRate += 0.1
                }
            }
        }
    }
}
