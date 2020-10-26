package kotlinmud.time.service

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinmud.event.factory.createDayEvent
import kotlinmud.event.factory.createPulseEvent
import kotlinmud.event.factory.createTickEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.logger
import kotlinmud.time.repository.findTime
import org.jetbrains.exposed.sql.transactions.transaction

const val TICKS_IN_DAY = 20
const val TICK_LENGTH_IN_SECONDS = 40

class TimeService(private val eventService: EventService) {
    private var pulse = 0
    private var lastSecond = 0
    private val logger = logger(this)
    private var time = findTime()

    init {
        logger.info("time service initialized at {} ticks", time)
    }

    fun isDaylight(): Boolean {
        val hour = getHour()
        return hour > 5 && hour < 21
    }

    suspend fun loop() {
        getSeconds().let {
            if (it != lastSecond) {
                lastSecond = it
                pulse()
                if (pulse > TICK_LENGTH_IN_SECONDS) {
                    pulse = 0
                    tick()
                }
            }
        }
    }

    private suspend fun pulse() {
        pulse++
        eventService.publish(createPulseEvent())
    }

    private suspend fun tick() {
        transaction { time.time += 1 }
        eventService.publish(createTickEvent())
        val hour = getHour()
        logger.info("tick occurred. hour of day :: {}, time :: {}", hour, time)
        if (hour == 0) {
            eventService.publish(createDayEvent())
            logger.info("a new day has started")
        }
    }

    private fun getHour(): Int {
        return transaction { time.time } % TICKS_IN_DAY
    }

    private fun getSeconds(): Int {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss")).toInt()
    }
}
