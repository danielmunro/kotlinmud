package kotlinmud.resource.service

import kotlinmud.biome.type.ResourceType
import kotlinmud.biome.type.SubstrateType
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.resource.helper.createResourceList
import kotlinmud.room.service.RoomService

class ResourceService(private val itemService: ItemService, private val roomService: RoomService) {
    private val resourceList = createResourceList()

    fun generateGrass() {
        roomService.filter {
            it.substrateType == SubstrateType.DIRT && it.resources.isEmpty()
        }.forEach {
            it.resources.add(ResourceType.BRUSH)
        }
    }

    fun tillResource(resource: ResourceType): List<Item> {
        return resourceList.find { it.resourceType == resource }?.createProduct(itemService) ?: listOf()
    }
}
