package kotlinmud.io.model

import kotlinmud.io.type.IOStatus

class PreAuthResponse(val request: PreAuthRequest, val status: IOStatus, val message: String)
