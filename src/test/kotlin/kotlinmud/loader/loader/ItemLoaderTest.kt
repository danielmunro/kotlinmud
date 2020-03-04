package kotlinmud.loader.loader

import assertk.assertThat
import assertk.assertions.isEqualTo
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
        val item = area.items.last()

        // expect
        assertThat(item.material).isEqualTo(Material.WOOD)
        assertThat(item.position).isEqualTo(Position.SHIELD)
    }
}
