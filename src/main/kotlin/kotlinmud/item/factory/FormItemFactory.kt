package kotlinmud.item.factory

import kotlinmud.helper.math.dice
import kotlinmud.helper.random.randomAmount
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.type.ItemType
import kotlinmud.item.type.Material
import kotlinmud.mob.dao.MobDAO
import kotlinmud.room.dao.RoomDAO

fun createDropFromReptile(mob: MobDAO, room: RoomDAO) {
    when (dice(1, 3)) {
        1 -> randomAmount(3) { createScale(room) }
        2 -> createTail(mob, room)
        3 -> createSmallFang(room)
    }
}

fun createLeather(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a patch of leather"
        description = "a patch of leather is here."
        type = ItemType.LEATHER
        material = Material.ORGANIC
        weight = 0.2
        room = roomDAO
        worth = 20
    }
}

fun createScale(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a scale"
        description = "a scale from a reptile has been left here."
        type = ItemType.OTHER
        material = Material.ORGANIC
        weight = 0.1
        room = roomDAO
        worth = 1
    }
}

fun createLargeFang(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a vicious fang"
        description = "a large, vicious fang is lying here."
        type = ItemType.OTHER
        material = Material.ORGANIC
        weight = 2.0
        room = roomDAO
        worth = 1
    }
}

fun createSmallFang(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a small fang"
        description = "a small, sharp fang is lying here."
        type = ItemType.OTHER
        material = Material.ORGANIC
        weight = 0.1
        room = roomDAO
        worth = 1
    }
}

fun createBlob(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a blob"
        description = "a small blob is here."
        type = ItemType.BLOB
        material = Material.ORGANIC
        weight = 3.0
        room = roomDAO
        worth = 30
    }
}

fun createThread(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a thread"
        description = "a thread is here."
        type = ItemType.THREAD
        material = Material.TEXTILE
        weight = 0.1
        room = roomDAO
        worth = 1
    }
}

fun createFeather(mob: MobDAO, roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "$mob's feather"
        description = "a feather from $mob is here."
        type = ItemType.FEATHER
        material = Material.ORGANIC
        weight = 0.0
        room = roomDAO
        worth = 1
    }
}

fun createBrains(mob: MobDAO, roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "brains of $mob"
        description = "the brains of $mob have been unceremoniously splashed on the ground."
        type = ItemType.ORGANS
        material = Material.ORGANIC
        weight = 2.5
        room = roomDAO
    }
}

fun createEntrails(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "bloody entrails"
        description = "bloody entrails are dashed across the ground."
        type = ItemType.ORGANS
        material = Material.ORGANIC
        weight = 5.0
        room = roomDAO
    }
}

fun createHeart(mob: MobDAO, roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a heart"
        description = "a heart of $mob is here."
        type = ItemType.ORGANS
        material = Material.ORGANIC
        weight = 2.0
        room = roomDAO
    }
}

fun createLiver(roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a liver"
        description = "a liver has been sliced from a corpse."
        type = ItemType.ORGANS
        material = Material.ORGANIC
        weight = 2.0
        room = roomDAO
    }
}

fun createTail(mob: MobDAO, roomDAO: RoomDAO): ItemDAO {
    return ItemDAO.new {
        name = "a tail of $mob"
        description = "a tail of $mob has been sliced off and left here."
        type = ItemType.ORGANS
        material = Material.ORGANIC
        weight = 1.0
        room = roomDAO
    }
}
