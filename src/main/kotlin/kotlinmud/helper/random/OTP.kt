package kotlinmud.helper.random

fun generateOTP(): String {
    return java.util.UUID.randomUUID().toString()
}
