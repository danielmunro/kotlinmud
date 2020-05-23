package kotlinmud.test

import kotlinmud.Noun
import kotlinmud.action.ActionService
import kotlinmud.app.createContainer
import kotlinmud.event.EventService
import kotlinmud.event.observer.Observers
import kotlinmud.item.ItemService
import kotlinmud.mob.MobService
import kotlinmud.service.FixtureService
import kotlinmud.service.RespawnService
import org.kodein.di.erased.instance

fun createTestService(): TestService {
    val container = createContainer(0, true)
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
