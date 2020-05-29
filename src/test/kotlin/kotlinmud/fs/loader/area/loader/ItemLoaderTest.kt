package kotlinmud.fs.loader.area.loader

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import assertk.assertions.isNotNull
import kotlinmud.affect.AffectType
import kotlinmud.fs.loader.AreaLoader
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.service.CURRENT_LOAD_SCHEMA_VERSION
import org.junit.Test

class ItemLoaderTest {
    @Test
    fun testLoadPropsMaterialAndPositionForItem() {
        // setup
        val area = AreaLoader("test_areas/midgard", CURRENT_LOAD_SCHEMA_VERSION).load()

        // given
        val item = area.items.first()

        // expect
        assertThat(item.material).isEqualTo(Material.ORGANIC)
        assertThat(item.position).isEqualTo(Position.NONE)
    }

    @Test
    fun testLoadPropsHitAndDam() {
        // setup
        val area = AreaLoader("test_areas/midgard", CURRENT_LOAD_SCHEMA_VERSION).load()

        // given
        val item = area.items.find { it.id == 3 }!!

        // expect
        assertThat(item.attributes.hit).isGreaterThan(0)
        assertThat(item.attributes.dam).isGreaterThan(0)
    }

    @Test
    fun testLoadAffects() {
        // setup
        val area = AreaLoader("test_areas/midgard", CURRENT_LOAD_SCHEMA_VERSION).load()

        // given
        val item = area.items.find { it.id == 50 }!!

        // expect
        assertThat(item.affects().getAffects()).hasSize(1)
        assertThat(item.affects().findByType(AffectType.DRUNK)).isNotNull()
    }
}
