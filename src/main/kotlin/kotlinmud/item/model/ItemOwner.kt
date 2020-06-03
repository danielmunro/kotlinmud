package kotlinmud.item.model

import kotlinmud.item.type.HasInventory

class ItemOwner(val item: Item, var owner: HasInventory)
