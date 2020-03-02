package kotlinmud.loader

import kotlinmud.item.Item
import kotlinmud.mob.Mob
import kotlinmud.room.Room

class Area(val rooms: List<Room>, val items: List<Item>, val mobs: List<Mob>)