package kotlinmud.test

import kotlinmud.Noun
import kotlinmud.createContainer
import kotlinmud.event.observer.Observers
import kotlinmud.service.ActionService
import kotlinmud.service.EventService
import kotlinmud.service.FixtureService
import kotlinmud.service.ItemService
import kotlinmud.service.MobService
import kotlinmud.service.RespawnService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer(0)
    val fix: FixtureService by container.instance()
    val mob: MobService by container.instance()
    val item: ItemService by container.instance()
    val act: ActionService by container.instance()
    val evt: EventService by container.instance()
    val respawnService: RespawnService by container.instance()
    val observers: Observers by container.instance()
    evt.observers = observers
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
