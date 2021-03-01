package kotlinmud.action.impl.shop

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.test.createTestService
import org.junit.Test

class ListTest {
    @Test
    fun testListSanity() {
        // setup
        val test = createTestService()
        test.createMob()

        // given
        val shopkeeper = test.createShopkeeper()
        val item1 = test.createItemBuilder()
                .level(1)
                .worth(100)
                .build()
        val item2 = test.createItemBuilder()
                .level(10)
                .worth(1)
                .build()
        val item3 = test.createItemBuilder()
                .level(20)
                .worth(3210)
                .build()
        shopkeeper.items.addAll(listOf(item1, item2, item3))

        // when
        val response = test.runAction("list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
"""
[lvl cost name]
   ${item1.level} ${item1.worth} $item1
  ${item2.level}    ${item2.worth} $item2
  ${item3.level} ${item3.worth} $item3""".trimMargin()
        )
    }
}
