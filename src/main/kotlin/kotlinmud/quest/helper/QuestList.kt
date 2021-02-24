package kotlinmud.quest.helper

import kotlinmud.mob.service.MobService
import kotlinmud.quest.impl.praetorians.FindCaptainBartok
import kotlinmud.quest.impl.praetorians.FindPraetorianRecruiter
import kotlinmud.quest.type.Quest

fun createQuestList(mobService: MobService): List<Quest> {
    return listOf(
        FindCaptainBartok(mobService),
        FindPraetorianRecruiter(mobService),
    )
}
