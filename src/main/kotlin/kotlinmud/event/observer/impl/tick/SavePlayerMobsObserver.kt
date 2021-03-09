package kotlinmud.event.observer.impl.tick

import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.service.MobService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinmud.mob.model.PlayerMob

class SavePlayerMobsObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val mapper = jacksonObjectMapper()
        println("save player mobs observer")
//        mobService.findPlayerMobs().forEach {
//            val data = mapper.writeValueAsString(it)
//            println(data)
//            val mapped: PlayerMob = mapper.readValue(data, PlayerMob::class.java)
//            println(if (it == mapped) "yes!" else "no")
//            println("done")
//        }
    }
}