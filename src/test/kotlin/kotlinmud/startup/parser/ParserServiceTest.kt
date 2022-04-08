package kotlinmud.startup.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.fail
import kotlinmud.startup.exception.DuplicateIdValidationException
import org.junit.Test

class ParserServiceTest {
    @Test
    fun testParseBasicMob() {
        // when
        val parserService = ParserService(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
~
"""
        ).parse()

        // then
        assertThat(parserService.mobs).hasSize(1)
        assertThat(parserService.mobs[0].id).isEqualTo(1)
        assertThat(parserService.mobs[0].name).isEqualTo("a red fox")
        assertThat(parserService.mobs[0].brief).isEqualTo("a small red fox is darting through the trees")
    }

    @Test
    fun testParseMobWithRole() {
        // when
        val parserService = ParserService(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
"""
        ).parse()

        // then
        assertThat(parserService.mobs).hasSize(1)

        // and
        val mob = parserService.mobs[0]
        assertThat(mob.id).isEqualTo(1)
        assertThat(mob.name).isEqualTo("a red fox")
        assertThat(mob.brief).isEqualTo("a small red fox is darting through the trees")

        // and
        assertThat(mob.keywords["job"]).isEqualTo("quest")
    }

    @Test
    fun testParseRespawns() {
        // when
        val parserService = ParserService(
            """
mob_respawns:
1 2 3 4
5 6 7 8
"""
        ).parse()

        // then
        assertThat(parserService.mobRespawns).hasSize(2)

        // and
        val respawn1 = parserService.mobRespawns[0]
        assertThat(respawn1.mobId).isEqualTo(1)
        assertThat(respawn1.maxAmountInRoom).isEqualTo(2)
        assertThat(respawn1.maxAmountInGame).isEqualTo(3)
        assertThat(respawn1.roomId).isEqualTo(4)

        // and
        val respawn2 = parserService.mobRespawns[1]
        assertThat(respawn2.mobId).isEqualTo(5)
        assertThat(respawn2.maxAmountInRoom).isEqualTo(6)
        assertThat(respawn2.maxAmountInGame).isEqualTo(7)
        assertThat(respawn2.roomId).isEqualTo(8)
    }

    @Test
    fun cannotDefineTwoRoomsWithSameId() {
        try {
            ParserService(
                """
rooms:
1. first room
a room~
~
1. second room
a room~
~
"""
            ).parse()
            fail("expected DuplicateIdValidationException")
        } catch (e: DuplicateIdValidationException) {
        }
    }

    @Test
    fun cannotDefineTwoMobsWithSameId() {
        try {
            ParserService(
                """
mobs:
1. first mob
a mob
~
~
1. second mob
a mob
~
~
"""
            ).parse()
            fail("expected DuplicateIdValidationException")
        } catch (e: DuplicateIdValidationException) {
        }
    }

    @Test
    fun cannotDefineTwoItemsWithSameId() {
        try {
            ParserService(
                """
items:
1. first item
an item
~
~
1. second item
an item
~
~
"""
            ).parse()
            fail("expected DuplicateIdValidationException")
        } catch (e: DuplicateIdValidationException) {
        }
    }

    @Test
    fun canParseItem() {
        // when
        val file = ParserService(
"""
items:
1. a test item
this is a brief
this is a
multiline
description~
weight 0.1~
material wool~
type equipment~
position held~
~
"""
        ).parse()

        val item = file.items[0]

        // then
        assertThat(item.id).isEqualTo(1)
        assertThat(item.brief).isEqualTo("this is a brief")
        assertThat(item.description).isEqualTo(
            """this is a
multiline
description"""
        )
        assertThat(item.keywords.get("weight")).isEqualTo("0.1")
        assertThat(item.keywords.get("material")).isEqualTo("wool")
        assertThat(item.keywords.get("type")).isEqualTo("equipment")
        assertThat(item.keywords.get("position")).isEqualTo("held")
    }
}
