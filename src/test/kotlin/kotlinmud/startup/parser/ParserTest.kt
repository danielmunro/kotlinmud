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

    @Test
    fun testParseRespawns() {
        // when
        val parser = Parser(
            """
mob_respawns:
1 2 3 4
5 6 7 8
"""
        ).parse()

        // then
        assertThat(parser.mobRespawns).hasSize(2)

        // and
        val respawn1 = parser.mobRespawns[0]
        assertThat(respawn1.mobId).isEqualTo(1)
        assertThat(respawn1.maxAmountInRoom).isEqualTo(2)
        assertThat(respawn1.maxAmountInGame).isEqualTo(3)
        assertThat(respawn1.roomId).isEqualTo(4)

        // and
        val respawn2 = parser.mobRespawns[1]
        assertThat(respawn2.mobId).isEqualTo(5)
        assertThat(respawn2.maxAmountInRoom).isEqualTo(6)
        assertThat(respawn2.maxAmountInGame).isEqualTo(7)
        assertThat(respawn2.roomId).isEqualTo(8)
    }
}
