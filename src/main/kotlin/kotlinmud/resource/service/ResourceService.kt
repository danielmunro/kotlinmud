package kotlinmud.resource.service

import kotlinmud.item.dao.ItemDAO
import kotlinmud.resource.helper.createResourceList
import kotlinmud.resource.repository.incrementResourceMaturity
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.insertGrassResource
import org.jetbrains.exposed.sql.transactions.transaction

class ResourceService {
    private val resourceList = createResourceList()

    fun incrementMaturity() {
        incrementResourceMaturity()
    }

    fun generateGrass() {
        insertGrassResource()
    }

    fun tillResource(resource: ResourceDAO): List<ItemDAO> {
        transaction { resource.delete() }
        return resourceList.find { it.resourceType == resource.type }?.createProduct() ?: listOf()
    }
}
