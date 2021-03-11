package kotlinmud.event.observer.tick

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.faction.type.FactionType
import kotlinmud.item.type.Position
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class SavePlayerMobsObserverTest {
    @Test
    fun testMapping() {
        // setup
        val test = createTestService()
        val mapper = jacksonObjectMapper()

        // given
        val mob = test.createPlayerMobBuilder().also {
            it.attributes[Attribute.STR] = 5
            it.attributes[Attribute.INT] = 1
            it.attributes[Attribute.DEX] = 2
            it.attributes[Attribute.HIT] = 3
            it.factionScores[FactionType.PRAETORIAN_GUARD] = 100
            it.quests[QuestType.FIND_PRAETORIAN_GUARD_RECRUITER] = QuestStatus.SUBMITTED
            it.quests[QuestType.JOIN_PRAETORIAN_GUARD] = QuestStatus.INITIALIZED
            it.skills[SkillType.BASH] = 1
            it.skills[SkillType.BERSERK] = 100
            it.affects.add(Affect(AffectType.BLESS, 1))
            it.affects.add(Affect(AffectType.STUNNED, 1, mapOf(Pair(Attribute.STR, -1))))
            it.currencies[CurrencyType.Copper] = 100
            it.currencies[CurrencyType.Silver] = 14
            it.currencies[CurrencyType.Gold] = 2
            it.items = mutableListOf(
                test.createItemBuilder().build()
            )
            it.equipped = mutableListOf(
                test.createItemBuilder().position(Position.HEAD).build()
            )
        }.build()

        // when
        test.dumpPlayerMob(mob)
        val rehydrated = test.hydratePlayerMob(mob.name)

        // then
        assertThat(mapper.writeValueAsString(rehydrated)).isEqualTo(mapper.writeValueAsString(mob))
    }
}
