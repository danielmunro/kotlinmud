package kotlinmud.service

import kotlin.streams.toList
import kotlinmud.exception.CraftException
import kotlinmud.exception.HarvestException
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.room.model.Room
import kotlinmud.world.ResourceType
import kotlinmud.world.createResourceList
import kotlinmud.world.resource.Resource

class CraftingService(
    private val itemService: ItemService,
    private val recipeList: List<Recipe>
) {
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

    fun craft(recipe: Recipe, hasInventory: HasInventory): List<Item> {
        val componentsList = createListOfItemTypesFromMap(recipe.getComponents())
        val toDestroy = createListOfItemsToDestroy(
            itemService.findAllByOwner(hasInventory).sortedBy { it.type }, componentsList)

        if (toDestroy.size < componentsList.size) {
            throw CraftException()
        }

        toDestroy.forEach { itemService.destroy(it) }

        return recipe.getProducts().stream().map {
            val newItem = it.copy()
            itemService.add(ItemOwner(newItem, hasInventory))
            newItem
        }.toList()
    }

    fun harvest(resourceType: ResourceType, room: Room, mob: Mob): List<Item> {
        room.resources.remove(resourceType)
        resources.find { it.resourceType == resourceType }?.let {
            val items = it.createProduct(itemService.createItemBuilderBuilder())
            items.forEach { item -> itemService.add(ItemOwner(item, mob)) }
            return items
        } ?: throw HarvestException()
    }
}
