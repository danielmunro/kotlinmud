package kotlinmud.quest.model

import kotlinmud.quest.type.QuestStatus

class Quest {
    var status: QuestStatus = QuestStatus.INITIALIZED
    var counter: Int = 0
}
