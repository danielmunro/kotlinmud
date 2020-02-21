package kotlinmud.test

import java.net.ServerSocket
import kotlinmud.createContainer
import kotlinmud.event.observer.createObservers
import kotlinmud.io.Server
import kotlinmud.item.Item
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.MobService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer()
    val fix: FixtureService by container.instance()
    val mob: MobService by container.instance()
    val act: ActionService by container.instance()
    val evt: EventService by container.instance()
    val server = Server(evt, ServerSocket())
    evt.observers = createObservers(server, mob)
    return TestService(fix, mob, act)
}

fun getIdentifyingWord(item: Item): String {
    return item.name.split(" ")[1]
}
