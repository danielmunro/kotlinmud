package kotlinmud.app

import kotlinmud.app.containerModule.ObserverModule
import kotlinmud.app.containerModule.createServiceModule
import kotlinmud.db.createConnection
import org.kodein.di.Kodein

fun createContainer(port: Int, test: Boolean = false): Kodein {
    return Kodein {
        createConnection()
        import(createServiceModule(port, test))
        import(ObserverModule)
    }
}
