package kotlinmud.string

fun leftPad(value: String, amount: Int): String {
    return String.format("%1$" + amount + "s", value)
}
