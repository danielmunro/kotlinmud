package kotlinmud.time.service

import kotlinmud.event.factory.createDayEvent
import kotlinmud.event.factory.createPulseEvent
import kotlinmud.event.factory.createTickEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.getEndOfDayMonth
import kotlinmud.helper.logger
import kotlinmud.time.dao.TimeDAO
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val START_OF_TIME = 10000
const val TICKS_IN_DAY = 20
const val TICK_LENGTH_IN_SECONDS = 40
const val DAYS_IN_MONTH = 30
const val DAYS_IN_YEAR = 365

class TimeService(
    private val eventService: EventService,
    private var time: TimeDAO
) {
    private var pulse = 0
    private var lastSecond = 0
    private val logger = logger(this)

    init {
        logger.info("time service initialized at {} ticks", time)
    }

    fun isDaylight(): Boolean {
        val hour = getHour()
        return hour > 5 && hour < 21
    }

    fun getDate(): String {
        val numberOfDays = getBaseTime() / TICKS_IN_DAY
        val dayOfMonth = numberOfDays % DAYS_IN_MONTH
        val month = (numberOfDays / DAYS_IN_MONTH) + 1
        val year = numberOfDays % DAYS_IN_YEAR

        return "it is the ${dayOfMonth}${getEndOfDayMonth(dayOfMonth)} day of month $month of year $year"
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

    suspend fun tick() {
        transaction { time.time += 1 }
        eventService.publish(createTickEvent())
        val hour = getHour()
        logger.info("tick occurred. hour of day :: {}, time :: {}", hour, time.time)
        if (hour == 0) {
            eventService.publish(createDayEvent())
            logger.info("a new day has started")
        }
    }

    private suspend fun pulse() {
        pulse++
        eventService.publish(createPulseEvent())
    }

    private fun getHour(): Int {
        return getBaseTime() % TICKS_IN_DAY
    }

    private fun getBaseTime(): Int {
        return transaction { time.time } + START_OF_TIME
    }

    private fun getSeconds(): Int {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss")).toInt()
    }
}
