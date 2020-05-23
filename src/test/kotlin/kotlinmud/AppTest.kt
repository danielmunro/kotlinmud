/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package kotlinmud

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.commit451.mailgun.Mailgun
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlinmud.action.ActionService
import kotlinmud.action.createActionsList
import kotlinmud.app.App
import kotlinmud.event.EventService
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.io.ClientService
import kotlinmud.io.NIOServer
import kotlinmud.item.ItemOwner
import kotlinmud.item.ItemService
import kotlinmud.mob.MobService
import kotlinmud.player.PlayerService
import kotlinmud.service.EmailService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
import kotlinmud.test.createTestService
import kotlinmud.world.World

class AppTest {
    @Test
    fun testAppSanityCheck() {
        // setup
        val itemService = ItemService()
        val world = World(listOf())
        val eventService = EventService()
        val server = NIOServer(ClientService(), eventService)
        val mobService = MobService(
            itemService,
            eventService,
            world,
            mutableListOf()
        )

        // when
        val app = App(
            eventService,
            mobService,
            TimeService(eventService),
            server,
            ActionService(
                mobService,
                ItemService(),
                eventService,
                WeatherService(),
                server,
                createActionsList(WorldSaver(world))
            ),
            PlayerService(
                EmailService(Mailgun.Builder("", "").build()),
                mutableListOf(),
                mutableListOf()
            )
        )

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
