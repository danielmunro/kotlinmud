package kotlinmud.resource.service

import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.resource.helper.createResourceList
import kotlinmud.resource.repository.incrementResourceMaturity
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.insertGrassResource
import org.jetbrains.exposed.sql.transactions.transaction

class ResourceService(private val itemService: ItemService) {
    private val resourceList = createResourceList()

    fun incrementMaturity() {
        incrementResourceMaturity()
    }

    fun generateGrass() {
        insertGrassResource()
    }

    fun tillResource(resource: ResourceDAO): List<Item> {
        transaction { resource.delete() }
        return resourceList.find { it.resourceType == resource.type }?.createProduct(itemService) ?: listOf()
    }
}
