package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.AreaModelBuilder
import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.parser.Token

class AreaSpec : Spec {
    override val tokens = listOf(
        Token.ID,
        Token.Name,
    )

    override fun builder(): Builder {
        return AreaModelBuilder()
    }
}
