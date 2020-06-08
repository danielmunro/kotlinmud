package kotlinmud.io.service

import kotlinmud.io.model.Client

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
}
