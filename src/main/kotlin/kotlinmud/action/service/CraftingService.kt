package kotlinmud.action.service

import kotlin.streams.toList
import kotlinmud.biome.helper.createResourceList
import kotlinmud.biome.type.ResourceType
import kotlinmud.exception.CraftException
import kotlinmud.exception.HarvestException
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.model.Item
import kotlinmud.item.model.ItemOwner
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Recipe
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.model.Mob
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room
import kotlinmud.world.resource.Resource
import org.jetbrains.exposed.sql.transactions.transaction

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

        fun createListOfItemsToDestroy(items: List<ItemDAO>, componentsList: List<ItemType>): List<ItemDAO> {
            val toDestroy: MutableList<ItemDAO> = mutableListOf()
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

    fun craft(recipe: Recipe, mob: MobDAO): List<ItemDAO> {
        val componentsList =
            createListOfItemTypesFromMap(recipe.getComponents())
        val toDestroy =
            createListOfItemsToDestroy(
                itemService.findAllByOwner(mob).sortedBy { it.type }, componentsList
            )

        if (toDestroy.size < componentsList.size) {
            throw CraftException()
        }

        toDestroy.forEach { itemService.destroy(it) }
        val products = recipe.getProducts()
        transaction {
            products.forEach {
                it.mobInventory = mob
            }
        }

        return products
    }

    fun harvest(resourceType: ResourceType, room: RoomDAO, mob: MobDAO): List<ItemDAO> {
        room.resources.remove(resourceType)
        resources.find { it.resourceType == resourceType }?.let {
            val products = it.createProduct()
            transaction {
                products.forEach { item ->
                    item.mobInventory = mob
                }
            }
            return products
        } ?: throw HarvestException()
    }
}
