package kotlinmud.action.impl.resource

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.type.ResourceType
import kotlinmud.test.helper.createTestService
import org.junit.Test

class HarvestTest {
    @Test
    fun testCanHarvestResource() {
        // setup
        val test = createTestService()
        val room = test.getStartRoom()
        val mob = test.createMob()
        val itemCount = mob.items.size

        // given
        room.resources.add(ResourceType.IRON_ORE)

        // when
        val response = test.runAction("harvest iron")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you successfully harvest iron ore.")
        assertThat(response.message.toObservers).isEqualTo("$mob harvests iron ore.")
        assertThat(response.delay).isGreaterThan(0)
        assertThat(mob.mv).isLessThan(mob.calc(Attribute.MV))
        assertThat(mob.items).hasSize(itemCount + 1)
        assertThat(room.resources.toList()).hasSize(0)
    }

    @Test
    fun testCannotHarvestResourceIfDoesNotExist() {
        // setup
        val test = createTestService()
        val mob = test.createMob()

        // when
        val response = test.runAction("harvest iron")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("you don't see that anywhere.")
        assertThat(response.delay).isEqualTo(0)
        assertThat(mob.mv).isEqualTo(mob.calc(Attribute.MV))
    }
}
