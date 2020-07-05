package kotlinmud.item.model

import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.HasInventory

class ItemOwner(val item: ItemDAO, var owner: HasInventory)
