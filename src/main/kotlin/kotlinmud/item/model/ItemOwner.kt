package kotlinmud.item.model

import kotlinmud.item.HasInventory

class ItemOwner(val item: Item, var owner: HasInventory)
