package kotlinmud.event.observer.impl.mob

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.helper.math.dice
import kotlinmud.helper.time.eventually
import kotlinmud.mob.race.impl.Sheep
import kotlinmud.mob.service.MobService
import kotlinmud.resource.impl.Wool

class SheepGrowWool(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val sheep = Sheep()
        mobService.findMobs {
            it.race == sheep && it.resources.size < 3
        }.forEach {
            if (dice(1, 2) == 1) {
                eventually {
                    it.resources.add(Wool())
                }
            }
        }
    }
}
