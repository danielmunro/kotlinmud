package kotlinmud.persistence.spec

import kotlinmud.persistence.model.builder.Builder
import kotlinmud.persistence.model.builder.ItemModelBuilder
import kotlinmud.persistence.token.BriefToken
import kotlinmud.persistence.token.DescriptionToken
import kotlinmud.persistence.token.IdToken
import kotlinmud.persistence.token.ItemRespawnToken
import kotlinmud.persistence.token.NameToken
import kotlinmud.persistence.token.PropsToken
import kotlinmud.room.model.Area

class ItemSpec(private val area: Area) : Spec {
    override val tokens = listOf(
        IdToken(),
        NameToken(),
        BriefToken(),
        DescriptionToken(),
        PropsToken(),
        ItemRespawnToken(),
    )

    override fun builder(): Builder {
        return ItemModelBuilder().also {
            it.area = area
        }
    }
}
