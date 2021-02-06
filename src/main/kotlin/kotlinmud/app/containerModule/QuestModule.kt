package kotlinmud.app.containerModule

import kotlinmud.app.Tag
import kotlinmud.quest.impl.praetorians.JoinPraetorianGuard
import kotlinmud.quest.type.Quest
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.provider

val QuestModule = Kodein.Module {
    bind<Quest>(tag = Tag.QUEST_RECRUIT_PRAETORIAN_GUARD) with provider {
        JoinPraetorianGuard()
    }
}
