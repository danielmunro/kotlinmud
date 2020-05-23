package kotlinmud.io

class ClientService {
    private val clients: MutableList<NIOClient> = mutableListOf()

    fun addClient(client: NIOClient) {
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
