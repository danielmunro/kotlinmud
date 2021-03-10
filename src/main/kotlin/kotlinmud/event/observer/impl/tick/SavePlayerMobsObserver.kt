package kotlinmud.event.observer.impl.tick

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinmud.event.impl.Event
import kotlinmud.event.observer.type.Observer
import kotlinmud.mob.builder.PlayerMobBuilder
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService

class SavePlayerMobsObserver(private val mobService: MobService) : Observer {
    override suspend fun <T> invokeAsync(event: Event<T>) {
        val mapper = jacksonObjectMapper()
        println("save player mobs observer")
        mobService.findPlayerMobs().forEach { mob ->
            val data = mapper.writeValueAsString(mob)
            println(data)
            val node: JsonNode = mapper.readTree(data)
            val rehydrated = PlayerMobBuilder(mobService).also {
                it.emailAddress = node.get("emailAddress").textValue()
                it.name = node.get("name").textValue()
                it.brief = node.get("brief").textValue()
                it.description = node.get("description").textValue()
                it.experienceToLevel = node.get("experienceToLevel").intValue()
                it.experience = node.get("experience").intValue()
                it.trains = node.get("trains").intValue()
                it.practices = node.get("practices").intValue()
                it.bounty = node.get("bounty").intValue()
                it.sacPoints = node.get("sacPoints").intValue()
                it.hunger = node.get("hunger").intValue()
                it.thirst = node.get("thirst").intValue()
                it.skillPoints = node.get("skillPoints").intValue()
//                it.factionScores
//                it.quests
                it.hp = node.get("hp").intValue()
                it.mana = node.get("mana").intValue()
                it.mv = node.get("mv").intValue()
                it.level = node.get("level").intValue()
                it.race = createRaceFromString(RaceType.valueOf(node.get("race").textValue()))
//                it.job = node.get("job")
//                it.specialization =
//                it.gender =
                it.wimpy = node.get("wimpy").intValue()
                it.savingThrows = node.get("savingThrows").intValue()
//                it.attributes
//                it.equipped
                it.maxItems = node.get("maxItems").intValue()
                it.maxWeight = node.get("maxWeight").intValue()
//                it.items
//                it.skills
//                it.affects
//                it.currencies
            }.build()
        }
    }
}
