package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.Token

interface Spec {
    val tokens: List<Token>
    fun builder(): Builder
}
