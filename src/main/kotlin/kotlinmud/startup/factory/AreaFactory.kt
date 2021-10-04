package kotlinmud.startup.factory

import kotlinmud.startup.model.AreaModel

fun createNoneAreaModel(): AreaModel {
    return AreaModel(0, "None")
}
