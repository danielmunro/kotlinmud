package kotlinmud.time.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import kotlinmud.test.helper.createTestService
import kotlinx.coroutines.runBlocking
import org.junit.Test

class TimeServiceTest {
    @Test
    fun testTimeServicePulseSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val timeService = test.getTimeService()

        // when
        runBlocking { timeService.tick() }

        // then
        assertThat(timeService.getDate()).isEqualTo("it is the 20th day of month 17 of year 135")
    }

    @Test
    fun testIsDaylightSanityCheck() {
        // setup
        val test = createTestService()

        // given
        val timeService = test.getTimeService()

        // when
        runBlocking { timeService.tick() }

        // then
        assertThat(timeService.isDaylight()).isFalse()
    }
}
