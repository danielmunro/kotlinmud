package kotlinmud.action.service

import kotlinmud.biome.type.ResourceType
import kotlinmud.exception.CraftException
import kotlinmud.exception.HarvestException
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.resource.helper.createResourceList
import kotlinmud.resource.type.Resource

class CraftingService(private val itemService: ItemService) {
    private val resources: List<Resource> = createResourceList()

    companion object {
        fun createListOfItemTypesFromMap(components: Map<ItemType, Int>): List<ItemType> {
            val componentsList: MutableList<ItemType> = mutableListOf()
            components.toSortedMap().forEach {
                for (i in 1 until it.value) {
                    componentsList.add(it.key)
                }
            }
            return componentsList
        }

        fun createListOfItemsToDestroy(items: List<Item>, componentsList: List<ItemType>): List<Item> {
            val toDestroy: MutableList<Item> = mutableListOf()
            var componentReq = 0
            items.forEach {
                if (componentReq < componentsList.size && it.type == componentsList[componentReq]) {
                    componentReq++
                    toDestroy.add(it)
                }
            }
            return toDestroy
        }
    }

    fun craft(recipe: Recipe, mob: Mob): List<Item> {
        val componentsList = createListOfItemTypesFromMap(recipe.getComponents())
        val toDestroy = createListOfItemsToDestroy(
            mob.items.sortedBy { it.type },
            componentsList
        )

        if (toDestroy.size < componentsList.size) {
            throw CraftException()
        }

        mob.items.removeAll(toDestroy)

        return createNewProductsFor(mob, recipe.getProducts(itemService))
    }

    fun harvest(resource: ResourceType, mob: Mob): List<Item> {
        return resources.find { it.resourceType == resource }?.let {
            createNewProductsFor(mob, it.createProduct(itemService))
        } ?: throw HarvestException()
    }

    private fun createNewProductsFor(mob: Mob, items: List<Item>): List<Item> {
        mob.items.addAll(items)
        return items
    }
}
