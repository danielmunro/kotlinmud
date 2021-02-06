package kotlinmud.quest.helper

import kotlinmud.quest.impl.praetorians.FindPraetorianRecruiter
import kotlinmud.quest.impl.praetorians.JoinPraetorianGuard
import kotlinmud.quest.type.Quest

fun createQuestList(): List<Quest> {
    return listOf(
        JoinPraetorianGuard(),
        FindPraetorianRecruiter(),
    )
}
