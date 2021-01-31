package kotlinmud.quest.helper

import kotlinmud.quest.impl.praetorians.FindARecruiter
import kotlinmud.quest.type.Quest

fun createQuestList(): List<Quest> {
    return listOf(
        FindARecruiter(),
    )
}
