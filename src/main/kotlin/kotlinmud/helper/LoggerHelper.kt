package kotlinmud.helper

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T : Any> logger(clazz: T): Logger {
    return LoggerFactory.getLogger(clazz::class.java)
}
