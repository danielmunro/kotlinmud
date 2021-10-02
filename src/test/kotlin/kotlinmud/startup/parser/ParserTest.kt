package kotlinmud.startup.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import org.junit.Test

class ParserTest {
    @Test
    fun testParseBasicMob() {
        // when
        val parser = Parser(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
~
"""
        ).parse()

        // then
        assertThat(parser.mobs).hasSize(1)
        assertThat(parser.mobs[0].id).isEqualTo(1)
        assertThat(parser.mobs[0].name).isEqualTo("a red fox")
        assertThat(parser.mobs[0].brief).isEqualTo("a small red fox is darting through the trees")
    }

    @Test
    fun testParseMobWithRole() {
        // when
        val parser = Parser(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
"""
        ).parse()

        // then
        assertThat(parser.mobs).hasSize(1)

        // and
        val mob = parser.mobs[0]
        assertThat(mob.id).isEqualTo(1)
        assertThat(mob.name).isEqualTo("a red fox")
        assertThat(mob.brief).isEqualTo("a small red fox is darting through the trees")

        // and
        assertThat(mob.keywords["job"]).isEqualTo("quest")
    }
}
