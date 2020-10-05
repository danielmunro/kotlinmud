package kotlinmud.event.observer.impl.regen

import kotlinmud.event.impl.Event
import kotlinmud.event.impl.RegenEvent
import kotlinmud.helper.math.dice
import kotlinmud.mob.skill.type.SkillType

fun meditationEvent(event: Event<*>) {
    with(event.subject as RegenEvent) {
        this.mob.getSkill(SkillType.MEDITATION)?.let {
            if (it.level > dice(1, 100)) {
                event.subject.manaRegenRate += 0.1
            }
        }
    }
}
