package kotlinmud.item

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.item.loader.ItemLoader
import kotlinmud.item.mapper.mapItem
import kotlinmud.item.type.Drink
import kotlinmud.item.type.Food
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.item.type.Position
import kotlinmud.mob.fight.DamageType
import kotlinmud.test.buf
import kotlinmud.test.createTestService
import org.junit.Test

class ItemTest {
    @Test
    fun testCanMapAndLoadItem() {
        // setup
        val test = createTestService()
        val item = test.itemBuilder()
            .name("foo")
            .description("bar")
            .type(ItemType.EQUIPMENT)
            .position(Position.WEAPON)
            .material(Material.COMPOSITE)
            .canOwn(true)
            .attackVerb("slap")
            .damageType(DamageType.COLD)
            .drink(Drink.MILK)
            .food(Food.MEAT_PIE)
            .quantity(2)
            .affects(
                mutableListOf(
                    AffectInstance(AffectType.BLESS, 1)
                )
            )
            .decayTimer(5)
            .hasInventory(true)
            .canOwn(false)
            .build()

        // given
        val data = mapItem(item)

        // when
        val model = ItemLoader(Tokenizer(data), 11)
            .load()
            .build()

        // then
        assertThat(buf(model)).isEqualTo(buf(item))
    }
}
