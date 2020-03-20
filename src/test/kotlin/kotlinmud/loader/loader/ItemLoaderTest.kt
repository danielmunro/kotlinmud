package kotlinmud.loader.loader

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import kotlinmud.item.Material
import kotlinmud.item.Position
import kotlinmud.loader.AreaLoader
import org.junit.Test

class ItemLoaderTest {
    @Test
    fun testLoadPropsMaterialAndPositionForItem() {
        // setup
        val area = AreaLoader("areas/midgard").load()

        // given
        val item = area.items.first()

        // expect
        assertThat(item.material).isEqualTo(Material.ORGANIC)
        assertThat(item.position).isEqualTo(Position.NONE)
    }

    @Test
    fun testLoadPropsHitAndDam() {
        // setup
        val area = AreaLoader("areas/midgard").load()

        // given
        val item = area.items.find { it.id == 3 }!!

        // expect
        assertThat(item.attributes.hit).isGreaterThan(0)
        assertThat(item.attributes.dam).isGreaterThan(0)
    }
}
