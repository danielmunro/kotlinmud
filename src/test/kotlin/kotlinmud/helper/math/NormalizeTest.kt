package kotlinmud.helper.math

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

class NormalizeTest {
    @Test
    fun testMinimum() {
        assertThat(normalizeInt(1, 0, 2)).isEqualTo(1)
    }

    @Test
    fun testMaximum() {
        assertThat(normalizeInt(1, 3, 2)).isEqualTo(2)
    }

    @Test
    fun testActual() {
        assertThat(normalizeInt(1, 2, 3)).isEqualTo(2)
    }
}
