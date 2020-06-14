package kotlinmud.helper.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import kotlinmud.test.ProbabilityTest
import org.junit.Test

class DiceTest {
    @Test
    fun testDnProbabilities() {
        // setup
        val iterations = 1000
        val prob = ProbabilityTest(iterations)

        // when
        while (prob.isIterating()) {
            val result = dN(1, 2)
            prob.decrementIteration(result == 1, result == 2)
        }

        // then
        assertThat(prob.getOutcome1() + prob.getOutcome2()).isEqualTo(iterations)
        assertThat(prob.getOutcome1()).isGreaterThan((iterations * 0.1).toInt())
        assertThat(prob.getOutcome2()).isGreaterThan((iterations * 0.1).toInt())
    }

    @Test
    fun testCoinFlip() {
        // setup
        val iterations = 1000
        val prob = ProbabilityTest(iterations)

        // when
        while (prob.isIterating()) {
            val result = coinFlip()
            prob.decrementIteration(result, !result)
        }

        // then
        assertThat(prob.getOutcome1()).isLessThan((iterations * 0.9).toInt())
        assertThat(prob.getOutcome2()).isLessThan((iterations * 0.9).toInt())
    }
}
