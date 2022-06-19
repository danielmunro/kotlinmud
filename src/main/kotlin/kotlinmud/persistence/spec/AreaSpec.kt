package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.AreaModelBuilder
import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.NameToken

class AreaSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
    )

    override fun builder(): Builder {
        return AreaModelBuilder()
    }
}
