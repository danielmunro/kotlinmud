package kotlinmud.app

import kotlinmud.app.containerModule.ObserverModule
import kotlinmud.app.containerModule.createServiceModule
import org.kodein.di.Kodein

fun createContainer(port: Int, test: Boolean = false): Kodein {
    return Kodein {
        import(createServiceModule(port, test))
        import(ObserverModule)
    }
}
