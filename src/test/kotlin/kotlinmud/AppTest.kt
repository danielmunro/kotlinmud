/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlinmud

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinmud.app.App
import kotlinmud.app.createContainer
import kotlinmud.item.ItemOwner
import kotlinmud.item.ItemService
import kotlinmud.test.createTestService
import org.kodein.di.erased.instance

class AppTest {
    @Test
    fun testAppSanityCheck() {
        // setup
        val container = createContainer(0)

        // when
        val app = container.instance<App>()

        // then
        assertNotNull(app, "app should have a greeting")
    }

    @Test
    fun testItemServiceAutoId() {
        val test = createTestService()
        val mob = test.createMob()
        val item = test.createItem(mob)
        val itemService = ItemService(mutableListOf(ItemOwner(item, mob)))
        val itemBuilder = itemService.createItemBuilderBuilder()()
            .name("foo")
            .description("foo")
            .level(1)
            .weight(0.0)
            .build()
        assertThat(itemBuilder.id).isEqualTo(4)
    }
}
