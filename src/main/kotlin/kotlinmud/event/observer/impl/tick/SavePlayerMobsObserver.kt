package kotlinmud.event.observer.impl.tick

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.MobService
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.room.service.RoomService

class SavePlayerMobsObserver(
    private val mobService: MobService,
    private val roomService: RoomService,
    private val specializations: List<Specialization>,
) : Observer {

    override suspend fun <T> invokeAsync(event: Event<T>) {
        val mapper = jacksonObjectMapper()
        println("save player mobs observer")
        mobService.findPlayerMobs().forEach { mob ->
        }
    }
}
