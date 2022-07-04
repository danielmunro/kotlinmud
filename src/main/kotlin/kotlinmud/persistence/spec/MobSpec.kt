package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.model.builder.MobModelBuilder
import kotlinmud.persistence.token.BriefToken
import kotlinmud.persistence.token.DescriptionToken
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.MobRespawnToken
import kotlinmud.persistence.token.NameToken
import kotlinmud.persistence.token.PropsToken

class MobSpec(private val area: String) : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        BriefToken(),
        DescriptionToken(),
        PropsToken(),
        MobRespawnToken(),
    )

    override fun builder(): Builder {
        return MobModelBuilder().also {
            it.area = area
        }
    }
}
