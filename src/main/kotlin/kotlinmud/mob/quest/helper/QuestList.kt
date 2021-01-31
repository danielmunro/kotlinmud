package kotlinmud.mob.quest.helper

import kotlinmud.mob.quest.impl.praetorians.FindARecruiter
import kotlinmud.mob.quest.type.Quest

fun createQuestList(): List<Quest> {
    return listOf(
        FindARecruiter(),
    )
}
