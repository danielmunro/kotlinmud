package kotlinmud.helper.time

import kotlinmud.time.service.TICK_LENGTH_IN_SECONDS
import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule

fun eventually(doThis: TimerTask.() -> Unit) {
    Timer()
        .schedule((Random().nextInt(TICK_LENGTH_IN_SECONDS) * 1000).toLong(), doThis)
}
