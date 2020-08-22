package kotlinmud.io.model

import kotlinmud.io.type.IOStatus
import kotlinmud.player.auth.type.AuthStep

class PreAuthResponse(
    val request: PreAuthRequest,
    val status: IOStatus,
    val message: String,
    val authStep: AuthStep
)
