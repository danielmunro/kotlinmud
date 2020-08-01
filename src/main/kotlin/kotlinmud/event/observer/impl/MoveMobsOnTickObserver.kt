package kotlinmud.event.observer.impl

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.helper.time.eventually
import kotlinmud.item.service.ItemService
import kotlinmud.mob.controller.MobController
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.service.MobService
import kotlinmud.mob.table.Mobs
import kotlinmud.mob.type.JobType
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select

class MoveMobsOnTickObserver(private val mobService: MobService, private val itemService: ItemService, private val eventService: EventService) :
    Observer {
    override val eventType: EventType = EventType.TICK

    override fun <T> processEvent(event: Event<T>) {
        MobDAO.wrapRows(Mobs.select {
            Mobs.isNpc eq true and
                    (Mobs.job eq JobType.SCAVENGER.value or
                            (Mobs.job eq JobType.FODDER.value or
                                    (Mobs.job eq JobType.PATROL.value)))
        }).forEach {
            eventually {
                MobController(mobService, itemService, eventService, it).move()
            }
        }
    }
}
