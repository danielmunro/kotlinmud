package kotlinmud.action.service

import kotlinmud.exception.CraftException
import kotlinmud.exception.HarvestException
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.resource.helper.createResourceList
import kotlinmud.resource.type.Resource
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.table.Resources
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction

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

    fun craft(recipe: Recipe, mob: Mob): List<ItemDAO> {
        val componentsList = createListOfItemTypesFromMap(recipe.getComponents())
        val toDestroy = createListOfItemsToDestroy(
            mob.items.sortedBy { it.type },
            componentsList
        )

        if (toDestroy.size < componentsList.size) {
            throw CraftException()
        }

        mob.items.removeAll(toDestroy)

        transaction {
            toDestroy.forEach {
                it.delete()
            }
        }

        return createNewProductsFor(mob, recipe.getProducts())
    }

    fun harvest(resource: ResourceDAO, mob: Mob): List<ItemDAO> {
        return transaction {
            removeResource(resource)
            resources.find { it.resourceType == resource.type }?.let {
                createNewProductsFor(mob, it.createProduct())
            } ?: throw HarvestException()
        }
    }

    private fun removeResource(resource: ResourceDAO) {
        Resources.deleteWhere(null as Int?, null as Int?) { Resources.id eq resource.id }
    }

    private fun createNewProductsFor(mob: Mob, items: List<ItemDAO>): List<ItemDAO> {
        mob.items.addAll(items)
        return items
    }
}
