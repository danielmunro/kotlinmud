package kotlinmud.time.service

import kotlinmud.event.factory.createDayEvent
import kotlinmud.event.factory.createPulseEvent
import kotlinmud.event.factory.createTickEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinmud.event.impl.DayEvent
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.PulseEvent
import kotlinmud.event.impl.TickEvent
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.logger
import kotlinmud.time.dao.TimeDAO
import kotlinmud.time.table.Times
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

const val TICKS_IN_DAY = 20
const val TICK_LENGTH_IN_SECONDS = 40

class TimeService(private val eventService: EventService) {
    private var pulse = 0
    private var lastSecond = 0
    private val logger = logger(this)
    private var time = transaction {
        Times.selectAll().firstOrNull()?.let {
            TimeDAO.wrapRow(it)
        } ?: TimeDAO.new {
            time = 0
        }
    }

    init {
        logger.info("time service initialized at {} ticks", time)
    }

    fun loop() {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ss")).toInt()
        if (time != lastSecond) {
            lastSecond = time
            pulse()
        }
    }

    private fun pulse() {
        pulse++
        eventService.publish(createPulseEvent())
        if (pulse > TICK_LENGTH_IN_SECONDS) {
            pulse = 0
            tick()
        }
    }

    private fun tick() {
        transaction { time.time += 1 }
        eventService.publish(createTickEvent())
        val hour = transaction { time.time } % TICKS_IN_DAY
        logger.info("tick occurred. hour of day :: {}, time :: {}", hour, time)
        if (hour == 0) {
            eventService.publish(createDayEvent())
            logger.info("a new day has started")
        }
    }
}
