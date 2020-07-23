package kotlinmud.fs.factory

import java.io.File
import kotlinmud.fs.constant.TIME_FILE
import kotlinmud.fs.constant.VERSION_FILE

fun versionFile(): File {
    return File(VERSION_FILE)
}

fun timeFile(): File {
    return File(TIME_FILE)
}
