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
        val item1 = test.createItem {
            it.level = 1
            it.worth = 100
        }
        val item2 = test.createItem {
            it.level = 10
            it.worth = 1
        }
        val item3 = test.createItem {
            it.level = 20
            it.worth = 3210
        }
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
