package kotlinmud.quest.type

enum class QuestRewardType(private val value: String) {
    Experience("experience"),
    FactionScore("faction score"),
    Item("item"),
    Currency("currency"),
}
