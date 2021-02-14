package kotlinmud.quest.helper

import kotlinmud.quest.impl.praetorians.FindCaptainBartok
import kotlinmud.quest.impl.praetorians.FindPraetorianRecruiter
import kotlinmud.quest.type.Quest

fun createQuestList(): List<Quest> {
    return listOf(
        FindCaptainBartok(),
        FindPraetorianRecruiter(),
    )
}
