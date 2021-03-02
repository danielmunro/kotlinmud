package kotlinmud.quest.helper

import kotlinmud.mob.service.MobService
import kotlinmud.quest.impl.praetorians.FindCaptainBartok
import kotlinmud.quest.impl.praetorians.FindPraetorianRecruiter
import kotlinmud.quest.type.Quest
import kotlinmud.room.service.RoomService

fun createQuestList(mobService: MobService, roomService: RoomService): List<Quest> {
    return listOf(
        FindCaptainBartok(mobService),
        FindPraetorianRecruiter(mobService, roomService),
    )
}
