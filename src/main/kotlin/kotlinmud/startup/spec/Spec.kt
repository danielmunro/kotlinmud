package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.token.Token

interface Spec {
    val tokens: List<Token>
    fun builder(): Builder
}
