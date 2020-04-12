package kotlinmud.fs.saver

import java.io.File
import kotlinmud.service.TimeService

fun saveTime(timeService: TimeService) {
    val file = File("./state/time.txt")
    file.writeText("#${timeService.getTime()}")
}
