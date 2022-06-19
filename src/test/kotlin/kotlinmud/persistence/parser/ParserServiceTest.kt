package kotlinmud.persistence.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.fail
import kotlinmud.persistence.exception.DuplicateIdValidationException
import kotlinmud.persistence.model.builder.RespawnSpec
import kotlinmud.persistence.model.builder.RespawnType
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
        val mob = parserService.mobs[0]
        assertThat(mob.id).isEqualTo(1)
        assertThat(mob.name).isEqualTo("a red fox")
        assertThat(mob.brief).isEqualTo("a small red fox is darting through the trees")
//        assertThat(mob.respawns).hasSize(1)
//        assertThat(mob.respawns[0]).isEqualTo(listOf(1, 1, 1))
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
~
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
    fun testParseMobWithMultipleProps() {
        // when
        val parserService = ParserService(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
worth 10~
~
"""
        ).parse()

        // then
        assertThat(parserService.mobs).hasSize(1)
        val mob = parserService.mobs[0]
        assertThat(mob.id).isEqualTo(1)
        assertThat(mob.name).isEqualTo("a red fox")
        assertThat(mob.brief).isEqualTo("a small red fox is darting through the trees")

        // and
        assertThat(mob.keywords["job"]).isEqualTo("quest")
        assertThat(mob.keywords["worth"]).isEqualTo("10")
    }

    @Test
    fun testParseMobWithRespawns() {
        // when
        val parserService = ParserService(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
~
1 1 1~
~
"""
        ).parse()

        // then
        assertThat(parserService.mobs).hasSize(1)
        val mob = parserService.mobs[0]
        assertThat(mob.id).isEqualTo(1)
        assertThat(mob.name).isEqualTo("a red fox")
        assertThat(mob.brief).isEqualTo("a small red fox is darting through the trees")

        // and
        assertThat(mob.respawns).hasSize(1)
        assertThat(mob.respawns[0]).isEqualTo(
            RespawnSpec(
                RespawnType.Mob,
                1,
                1,
                1,
            )
        )
    }

    @Test
    fun testParseMobWithMultipleRespawns() {
        // when
        val parserService = ParserService(
            """
mobs:
1. a red fox
a small red fox is darting through the trees
~
~
1 1 1~
2 2 2~
~
"""
        ).parse()

        // then
        assertThat(parserService.mobs).hasSize(1)
        val mob = parserService.mobs[0]
        assertThat(mob.respawns).hasSize(2)
        assertThat(mob.respawns[0]).isEqualTo(
            RespawnSpec(
                RespawnType.Mob,
                1,
                1,
                1,
            )
        )
        assertThat(mob.respawns[1]).isEqualTo(
            RespawnSpec(
                RespawnType.Mob,
                2,
                2,
                2,
            )
        )
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
~
1. second mob
a mob
~
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
~
1. second item
an item
~
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
room 1 2 3~
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
        assertThat(item.respawns).hasSize(1)

        // and
        val respawn = item.respawns.first()
        assertThat(respawn.type).isEqualTo(RespawnType.Room)
        assertThat(respawn.maxAmount).isEqualTo(1)
        assertThat(respawn.maxInGame).isEqualTo(2)
        assertThat(respawn.targetId).isEqualTo(3)
    }
}
