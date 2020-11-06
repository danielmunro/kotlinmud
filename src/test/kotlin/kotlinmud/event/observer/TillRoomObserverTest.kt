package kotlinmud.event.observer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.biome.type.SubstrateType
import kotlinmud.resource.factory.createWildGrass
import kotlinmud.test.ProbabilityTest
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class TillRoomObserverTest {
    @Test
    fun testTillSanity() {
        // setup
        val test = createTestService()
        val room = test.getStartRoom()
        val prob = ProbabilityTest(100)

        while (prob.isIterating()) {
            // given
            transaction {
                room.substrate = SubstrateType.DIRT
                createWildGrass().room = room
                room.items.empty()
            }

            // when
            test.runAction("till")
            val count = transaction { room.items.count() }
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
        transaction {
            room.substrate = SubstrateType.TILLED_DIRT
            createWildGrass().room = room
        }

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
        transaction {
            room.substrate = SubstrateType.ROCK
        }

        // when
        val response = test.runAction("till")

        // then
        assertThat(response.message.toActionCreator).isEqualTo("You cannot till here.")
    }
}
