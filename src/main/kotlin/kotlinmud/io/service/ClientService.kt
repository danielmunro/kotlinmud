package kotlinmud.io.service

import kotlinmud.io.model.Client
import kotlinmud.mob.model.Mob

class ClientService {
    private val clients: MutableList<Client> = mutableListOf()

    fun addClient(client: Client) {
        clients.add(client)
    }

    fun decrementDelays() {
        clients.forEach {
            if (it.delay > 0) {
                it.delay--
            }
        }
    }

    fun getClientForMob(mob: Mob): Client? {
        return clients.find { it.mob == mob }
    }
}
