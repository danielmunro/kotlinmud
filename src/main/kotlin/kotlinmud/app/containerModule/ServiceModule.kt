package kotlinmud.app.containerModule

import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.event.service.EventService
import kotlinmud.generator.service.FixtureService
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.specialization.helper.createSpecializationList
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.factory.createEmailService
import kotlinmud.player.factory.createEmailServiceMock
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.helper.questList
import kotlinmud.quest.service.QuestService
import kotlinmud.resource.service.ResourceService
import kotlinmud.respawn.helper.itemAreaRespawns
import kotlinmud.respawn.helper.itemMobRespawns
import kotlinmud.respawn.helper.mobRespawns
import kotlinmud.respawn.service.ItemAreaRespawnService
import kotlinmud.respawn.service.ItemMobRespawnService
import kotlinmud.respawn.service.MobRespawnService
import kotlinmud.respawn.service.RespawnService
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Area
import kotlinmud.time.repository.findTime
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService
import kotlinmud.world.service.AreaBuilderService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
import org.kodein.di.erased.factory
import org.kodein.di.erased.instance
import org.kodein.di.erased.singleton
import java.net.ServerSocket

fun createServiceModule(port: Int, test: Boolean): Kodein.Module {
    return Kodein.Module {
        bind<ServerSocket>() with singleton { ServerSocket(port) }
        bind<ClientService>() with singleton { ClientService() }
        bind<ServerService>() with singleton {
            ServerService(instance(), instance(), port)
        }
        bind<FixtureService>() with singleton { FixtureService() }
        bind<EventService>() with singleton { EventService() }
        bind<ItemService>() with singleton { ItemService() }
        bind<RoomService>() with singleton { RoomService() }
        bind<MobService>() with singleton {
            MobService(
                instance(),
                instance()
            )
        }
        bind<WeatherService>() with singleton { WeatherService() }
        bind<EmailService>() with singleton {
            if (test) {
                createEmailServiceMock()
            } else {
                createEmailService()
            }
        }
        bind<PlayerService>() with singleton {
            PlayerService(instance(), instance(), instance(), instance(), createSpecializationList())
        }
        bind<AuthStepService>() with singleton {
            val playerService = instance<PlayerService>()
            AuthStepService(instance(), instance(), playerService).also {
                playerService.setAuthStepService(it)
            }
        }
        bind<TimeService>() with singleton {
            TimeService(instance(), findTime())
        }
        bind<QuestService>() with singleton {
            QuestService(questList)
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance(),
                createActionContextBuilder(
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                    instance(),
                ),
                createActionsList()
            )
        }
        bind<ContextBuilderService>() with singleton {
            ContextBuilderService(
                instance(),
                instance(),
                instance(),
                instance(),
                createSkillList(),
                createRecipeList()
            )
        }
        bind<ResourceService>() with singleton {
            ResourceService(instance(), instance())
        }
        bind<ItemAreaRespawnService>() with singleton {
            ItemAreaRespawnService(instance(), instance(), itemAreaRespawns)
        }
        bind<MobRespawnService>() with singleton {
            MobRespawnService(instance(), instance(), mobRespawns)
        }
        bind<ItemMobRespawnService>() with singleton {
            ItemMobRespawnService(instance(), itemMobRespawns)
        }
        bind<RespawnService>() with singleton {
            RespawnService(
                instance(),
                instance(),
                instance(),
            )
        }
        bind<AreaBuilderService>() with factory { area: Area ->
            AreaBuilderService(
                instance(),
                instance(),
                instance(),
                area,
            )
        }
    }
}
