package kotlinmud.test

class ProbabilityTest(private var iterations: Int = 1000) {
    private var outcome1: Int = 0
    private var outcome2: Int = 0

    fun isIterating(): Boolean {
        return iterations > 0
    }

    fun test(fn1: () -> Boolean, fn2: () -> Boolean) {
        while (isIterating()) {
            decrementIteration(fn1(), fn2())
        }
    }

    fun decrementIteration(outcome1Result: Boolean, outcome2Result: Boolean) {
        if (!isIterating()) {
            return
        }
        iterations--
        if (outcome1Result) {
            outcome1++
        }
        if (outcome2Result) {
            outcome2++
        }
    }

    fun getOutcome1(): Int {
        return outcome1
    }

    fun getOutcome2(): Int {
        return outcome2
    }
}
