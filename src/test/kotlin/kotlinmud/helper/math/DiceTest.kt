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
            val result = dice(1, 2)
            prob.decrementIteration(result == 1, result == 2)
        }

        // then
        assertThat(prob.getOutcome1() + prob.getOutcome2()).isEqualTo(iterations)
        assertThat(prob.getOutcome1()).isGreaterThan((iterations * 0.1).toInt())
        assertThat(prob.getOutcome2()).isGreaterThan((iterations * 0.1).toInt())
    }

    @Test
    fun testDnWithMultipleProbabilities() {
        // setup
        val iterations = 1000
        val prob1 = ProbabilityTest(iterations)
        val prob2 = ProbabilityTest(iterations)

        // when
        while (prob1.isIterating()) {
            val result = dice(1, 4)
            prob1.decrementIteration(
                result == 1,
                result == 2
            )
            prob2.decrementIteration(
                result == 3,
                result == 4
            )
        }

        // then
        assertThat(prob1.getOutcome1()).isGreaterThan(0)
        assertThat(prob1.getOutcome2()).isGreaterThan(0)
        assertThat(prob2.getOutcome1()).isGreaterThan(0)
        assertThat(prob2.getOutcome2()).isGreaterThan(0)
        assertThat(
            prob1.getOutcome1() +
                    prob1.getOutcome2() +
                    prob2.getOutcome1() +
                    prob2.getOutcome2()
        ).isEqualTo(iterations)
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
        assertThat(prob.getOutcome1() + prob.getOutcome2()).isEqualTo(iterations)
    }
}
