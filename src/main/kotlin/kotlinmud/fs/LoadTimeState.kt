package kotlinmud.fs

import java.io.File
import kotlinmud.fs.loader.TimeLoader

fun loadTimeState(isTest: Boolean = false): Int {
    return if (File("./state/time.txt").exists() && !isTest) {
        println("loading time state")
        TimeLoader().load()
    } else {
        0
    }
}
