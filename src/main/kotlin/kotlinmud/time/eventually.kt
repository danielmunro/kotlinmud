package kotlinmud.time

import java.util.*
import kotlin.concurrent.schedule
import kotlinmud.io.TICK_LENGTH_IN_SECONDS

fun eventually(doThis: TimerTask.() -> Unit) {
    Timer()
        .schedule((Random().nextInt(TICK_LENGTH_IN_SECONDS) * 1000).toLong(), doThis)
}
