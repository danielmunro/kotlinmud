package kotlinmud.test

import java.net.ServerSocket
import kotlinmud.Noun
import kotlinmud.createContainer
import kotlinmud.event.observer.createObservers
import kotlinmud.io.Server
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService
import kotlinmud.service.TimeService
import kotlinmud.service.WeatherService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer()
    val fix: FixtureService by container.instance()
    val mob: MobService by container.instance()
    val item: ItemService by container.instance()
    val act: ActionService by container.instance()
    val evt: EventService by container.instance()
    val server = Server(evt, ServerSocket(), TimeService(evt))
    val respawnService: RespawnService by container.instance()
    val weatherService: WeatherService by container.instance()
    val itemService: ItemService by container.instance()
    evt.observers = createObservers(server, mob, evt, respawnService, weatherService, itemService)
    return TestService(
        fix,
        mob,
        item,
        act,
        respawnService,
        evt
    )
}

fun getIdentifyingWord(noun: Noun): String {
    return noun.name.split(" ").first { it.length > 3 }
}
