package kotlinmud.io.factory

import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus

fun createOkPreAuthResponse(request: PreAuthRequest, message: String): PreAuthResponse {
    return PreAuthResponse(request, IOStatus.OK, message)
}

fun createFailedPreAuthResponse(request: PreAuthRequest, message: String): PreAuthResponse {
    return PreAuthResponse(request, IOStatus.FAILED, message)
}

fun createErrorPreAuthResponse(request: PreAuthRequest, message: String): PreAuthResponse {
    return PreAuthResponse(request, IOStatus.ERROR, message)
}
