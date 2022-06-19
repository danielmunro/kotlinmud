package kotlinmud.persistence.factory

import kotlinmud.persistence.model.AreaModel

fun createNoneAreaModel(): AreaModel {
    return AreaModel(0, "None")
}
