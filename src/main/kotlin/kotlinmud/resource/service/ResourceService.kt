package kotlinmud.resource.service

import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.resource.helper.createResourceList
import kotlinmud.resource.repository.incrementResourceMaturity
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.repository.insertGrassResource
import kotlinmud.room.service.RoomService
import org.jetbrains.exposed.sql.transactions.transaction

class ResourceService(private val itemService: ItemService, private val roomService: RoomService) {
    private val resourceList = createResourceList()

    fun incrementMaturity() {
        incrementResourceMaturity()
    }

    fun generateGrass() {
        roomService.filter {
            it.substrateType == SubstrateType.DIRT && it.resources.isEmpty()
        }.forEach {
            it.resources.add(ResourceType.BRUSH)
        }
    }

    fun tillResource(resource: ResourceDAO): List<Item> {
        transaction { resource.delete() }
        return resourceList.find { it.resourceType == resource.type }?.createProduct(itemService) ?: listOf()
    }
}
