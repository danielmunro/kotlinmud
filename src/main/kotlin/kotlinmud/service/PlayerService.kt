package kotlinmud.service

import kotlinmud.io.IOStatus
import kotlinmud.io.NIOClient
import kotlinmud.io.PreAuthRequest
import kotlinmud.io.PreAuthResponse
import kotlinmud.player.Player
import kotlinmud.player.authStep.AuthStep
import kotlinmud.player.authStep.EmailAuthStep

class PlayerService {
    private val players: MutableList<Player> = mutableListOf()
    private val preAuthClients: Map<NIOClient, AuthStep> = mutableMapOf()
    private val loggedInPlayers: Map<NIOClient, Player> = mutableMapOf()

    fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep()
        val response = authStep.handlePreAuthRequest(request)
        if (response.status == IOStatus.OK) {
            preAuthClients.plus(Pair(request.client, authStep.getNextAuthStep()))
        }
        return response
    }

    private fun loginPlayer(client: NIOClient, player: Player) {
        loggedInPlayers.plus(Pair(client, player))
    }
}
