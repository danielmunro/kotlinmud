package main.kotlin.kotlinmud

class Calculator {

    fun calculate(a: Int, b: Int, operation: String): String {
        return when (operation) {
            "add" -> calc(a, b, ::add).toString()
            "sub" -> calc(a, b, ::sub).toString()
            "div" -> calc(a.toDouble(), b.toDouble(), ::div).toString()
            "multi" -> calc(a, b, ::multi).toString()
            else -> {
                "Something whent wrong"
            }
        }
    }

    // A Calculator (functional programming)
    private fun <T> calc(a: T, b: T, operation: (T, T) -> T): T {
        return operation(a, b)
    }

    private fun add(a: Int, b: Int): Int = a + b
    private fun sub(a: Int, b: Int): Int = a - b
    private fun div(a: Double, b: Double): Double = a / b
    private fun multi(a: Int, b: Int): Int = a * b
}
