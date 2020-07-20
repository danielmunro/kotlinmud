package kotlinmud.action.service

import kotlinmud.biome.helper.createResourceList
import kotlinmud.exception.CraftException
import kotlinmud.exception.HarvestException
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Recipe
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.table.Resources
import kotlinmud.world.resource.Resource
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

class CraftingService(
    private val itemService: ItemService
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

    fun harvest(resource: ResourceDAO, room: RoomDAO, mob: MobDAO): List<ItemDAO> {
        return transaction {
            Resources.deleteWhere(null, 0 as Int) { Resources.id eq resource.id }
            resources.find { it.resourceType == resource.type }?.let {
                val products = it.createProduct()
                transaction {
                    products.forEach { item ->
                        item.mobInventory = mob
                    }
                }
                return@let products
            } ?: throw HarvestException()
        }
    }
}
