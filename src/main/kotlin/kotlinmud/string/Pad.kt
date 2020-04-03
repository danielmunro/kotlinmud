package kotlinmud.string

fun leftPad(value: String, amount: Int): String {
    val pad = (amount - value.length + 1).coerceAtLeast(0)
    return String.format("%1$" + pad + "s", value)
}
