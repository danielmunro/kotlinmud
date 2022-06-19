package kotlinmud.persistence.service

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.race.impl.Canid
import kotlinmud.mob.type.JobType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class StartupServiceTest {
    @Test
    fun testStartupCanRespawnItem() {
        // setup
        val test = createTestService(false)

        // given
        val startup = test.createStartupService(
            listOf(
"""
area:
1. LorimirForest

rooms:
1. a room
a test room
a test room~
~

items:
1. a test item
this is a brief
this is a
multiline
description~
weight 0.1~
material iron~
type equipment~
position held~
~
room 1 1 1~
~
"""
            )
        )

        // when
        startup.hydrateWorld()

        val item = test.findItem {
            it.id == 1
        }!!

        // then
        assertThat(item.weight).isEqualTo(0.1)
        assertThat(item.material).isEqualTo(Material.IRON)
        assertThat(item.type).isEqualTo(ItemType.EQUIPMENT)
        assertThat(item.position).isEqualTo(Position.HELD)
    }

    @Test
    fun testStartupCanRespawnMob() {
        // setup
        val test = createTestService(false)

        // given
        val startup = test.createStartupService(
            listOf(
                """
area:
1. LorimirForest

rooms:
1. a test room
a test room~
~

mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
race canid~
~
1 1 1~
~
"""
            )
        )

        // when
        startup.hydrateWorld()

        val mob = test.findMob {
            it.id == 1
        }!!

        // then
        assertThat(mob.job).isEqualTo(JobType.QUEST)
        assertThat(mob.race).isEqualTo(Canid())
    }

    @Test
    fun testStartupCanRespawnItemToMobAsEquipment() {
        // setup
        val test = createTestService(false)

        // given
        val startup = test.createStartupService(
            listOf(
                """
area:
1. LorimirForest

rooms:
1. a test room
a test room~
~

mobs:
1. a red fox
a small red fox is darting through the trees
~
job quest~
race canid~
~
1 1 1~
~

items:
1. a test item
this is a brief
this is a
multiline
description~
weight 0.1~
material iron~
type equipment~
position held~
~
mob 1 1 1~
~
"""
            )
        )

        // when
        startup.hydrateWorld()

        val mob = test.findMob {
            it.id == 1
        }!!

        // then
        assertThat(mob.equipped).hasSize(1)
        assertThat(mob.equipped.first().name).isEqualTo("a test item")
    }
}
