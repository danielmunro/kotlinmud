package kotlinmud.action.impl

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlinmud.mob.type.JobType
import kotlinmud.test.createTestService
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class ListTest {
    @Test
    fun testListSanity() {
        // setup
        val test = createTestService()
        test.respawnWorld()
        val mob = test.createMob()

        // given
        val shopkeeper = test.createMob()
        val item1 = test.createItem()
        val item2 = test.createItem()
        val item3 = test.createItem()
        transaction {
            shopkeeper.job = JobType.SHOPKEEPER
            item1.mobInventory = shopkeeper
            item1.level = 1
            item1.worth = 100
            item2.mobInventory = shopkeeper
            item2.level = 10
            item2.worth = 1
            item3.mobInventory = shopkeeper
            item3.level = 20
            item3.worth = 3210
        }

        // when
        val response = test.runAction(mob, "list")

        // then
        assertThat(response.message.toActionCreator).isEqualTo(
"""
[lvl cost name]
   1    0 a sword
   ${item1.level} ${item1.worth} $item1
  ${item2.level}    ${item2.worth} $item2
  ${item3.level} ${item3.worth} $item3""".trimMargin())
    }
}
