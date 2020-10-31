package kotlinmud.resource.service

import kotlinmud.resource.repository.incrementResourceMaturity
import kotlinmud.room.repository.insertGrassResource

class ResourceService {
    fun incrementMaturity() {
        incrementResourceMaturity()
    }

    fun generateGrass() {
        insertGrassResource()
    }
}
