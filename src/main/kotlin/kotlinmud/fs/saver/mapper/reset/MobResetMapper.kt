package kotlinmud.fs.saver.mapper.reset

import kotlinmud.fs.loader.model.reset.MobReset

fun mapMobReset(mobReset: MobReset): String {
    return "mobId: ${mobReset.mobId}, roomId: ${mobReset.roomId}, maxInRoom: ${mobReset.maxInRoom}, maxInWorld: ${mobReset.maxInWorld}~"
}
