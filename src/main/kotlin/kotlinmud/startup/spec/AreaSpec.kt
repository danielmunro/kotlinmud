package kotlinmud.startup.spec

import kotlinmud.startup.model.builder.AreaModelBuilder
import kotlinmud.startup.model.builder.Builder
import kotlinmud.startup.token.IdToken
import kotlinmud.startup.token.NameToken

class AreaSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
    )

    override fun builder(): Builder {
        return AreaModelBuilder()
    }
}
