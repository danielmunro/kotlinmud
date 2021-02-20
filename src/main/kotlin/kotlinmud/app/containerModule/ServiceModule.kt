package kotlinmud.app.containerModule

import kotlinmud.action.helper.createActionContextBuilder
import kotlinmud.action.helper.createActionsList
import kotlinmud.action.service.ActionService
import kotlinmud.action.service.ContextBuilderService
import kotlinmud.biome.helper.createBiomes
import kotlinmud.event.service.EventService
import kotlinmud.generator.service.FixtureService
import kotlinmud.generator.service.MobGeneratorService
import kotlinmud.io.service.ClientService
import kotlinmud.io.service.ServerService
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.factory.createEmailService
import kotlinmud.player.factory.createEmailServiceMock
import kotlinmud.player.service.EmailService
import kotlinmud.player.service.PlayerService
import kotlinmud.quest.service.QuestService
import kotlinmud.resource.service.ResourceService
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService
import kotlinmud.world.itrias.lorimir.getLorimirItemRespawns
import kotlinmud.world.itrias.lorimir.getLorimirMobRespawns
import kotlinmud.world.service.RespawnService
import org.kodein.di.Kodein
import org.kodein.di.erased.bind
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
        bind<WeatherService>() with singleton { WeatherService() }
        bind<EmailService>() with singleton {
            if (test) {
                createEmailServiceMock()
            } else {
                createEmailService()
            }
        }
        bind<PlayerService>() with singleton {
            PlayerService(instance(), instance())
        }
        bind<AuthStepService>() with singleton {
            val playerService = instance<PlayerService>()
            AuthStepService(playerService).also {
                playerService.setAuthStepService(it)
            }
        }
        bind<TimeService>() with singleton {
            TimeService(instance())
        }
        bind<QuestService>() with singleton {
            QuestService()
        }
        bind<ActionService>() with singleton {
            ActionService(
                instance(),
                instance(),
                createActionContextBuilder(
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
        bind<MobService>() with singleton {
            MobService(
                instance(),
                instance(),
                createSkillList()
            )
        }
        bind<ContextBuilderService>() with singleton {
            ContextBuilderService(
                instance(),
                instance(),
                instance(),
                createSkillList(),
                createRecipeList()
            )
        }
        bind<MobGeneratorService>() with singleton {
            MobGeneratorService(createBiomes())
        }
        bind<ResourceService>() with singleton {
            ResourceService()
        }
        bind<RespawnService>() with singleton {
            RespawnService(
                getLorimirItemRespawns(),
                getLorimirMobRespawns()
            )
        }
    }
}
