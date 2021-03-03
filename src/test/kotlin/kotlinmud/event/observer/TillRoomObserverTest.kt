package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.test.model.ProbabilityTest
import kotlinmud.test.helper.createTestService
import org.junit.Test

class TillRoomObserverTest {
    @Test
    fun testTillSanity() {
        // setup
        val test = createTestService()
        val room = test.createRoomBuilder()
            .substrate(SubstrateType.DIRT)
            .build()
        val prob = ProbabilityTest(100)
        val mob = test.createMobBuilder()
            .room(room)
            .build()

        while (prob.isIterating()) {
            // given
            room.resources.add(ResourceType.BRUSH)
            room.items.clear()

            // when
            test.runAction(mob, "till")
            val count = room.items.count()
            prob.decrementIteration(count > 0, count == 0)
        }

        // then
        assertThat(prob.getOutcome1()).isGreaterThan(0)
    }

    @Test
    fun testTillWorksWithTilledGround() {
        // setup
        val test = createTestService()
        val room = test.getStartRoom()

        // given
        room.substrateType = SubstrateType.TILLED_DIRT
        room.resources.add(ResourceType.BRUSH)

        // when
        val response = test.runAction("till")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You till the ground.")
    }

    @Test
    fun testTillDoesNotWorkWithOtherSubstrate() {
        // setup
        val test = createTestService()
        val room = test.getStartRoom()

        // given
        room.substrateType = SubstrateType.ROCK

        // when
        val response = test.runAction("till")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You cannot till here.")
    }
}
