package kotlinmud.string

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class PadTest {
    @Test
    fun testPadSanity() {
        // given
        val pad = leftPad("1", 3)

        // expect
        assertThat(pad).isEqualTo("  1")
    }

    @Test
    fun testPadSanity2() {
        // given
        val pad = leftPad("hello world", 3)

        // expect
        assertThat(pad).isEqualTo("hello world")
    }
}
