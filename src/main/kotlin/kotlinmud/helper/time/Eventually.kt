package kotlinmud.helper.time

import java.util.Random
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule
import kotlinmud.service.TICK_LENGTH_IN_SECONDS

fun eventually(doThis: TimerTask.() -> Unit) {
    Timer()
        .schedule((Random().nextInt(TICK_LENGTH_IN_SECONDS) * 1000).toLong(), doThis)
}
