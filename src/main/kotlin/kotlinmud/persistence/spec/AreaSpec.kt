package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.AreaModelBuilder
import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.NameToken
import kotlinmud.persistence.token.PropsToken

class AreaSpec : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        PropsToken(),
    )

    override fun builder(): Builder {
        return AreaModelBuilder()
    }
}
