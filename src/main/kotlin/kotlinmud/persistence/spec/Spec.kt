package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.token.Token

interface Spec {
    val tokens: List<Token>
    fun builder(): Builder
}
