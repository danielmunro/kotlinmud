package kotlinmud.helper.string

fun leftPad(value: String, amount: Int): String {
    val pad = (amount - value.length)
    if (pad < 1) {
        return value
    }
    var buffer = ""
    for (i in 0..pad) {
        buffer += " "
    }
    return buffer + value
}
