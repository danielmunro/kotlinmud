package kotlinmud.mob.provider

import java.io.EOFException
import java.io.File
import kotlinmud.fs.PLAYER_MOBS_FILE
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.loader.area.loader.MobLoader
import kotlinmud.mob.model.Mob

fun loadMobs(loadSchemaToUse: Int): MutableList<Mob> {
    val file = File(PLAYER_MOBS_FILE)
    if (!file.exists()) {
        return mutableListOf()
    }
    val tokenizer = Tokenizer(file.readText())
    val mobLoader = MobLoader(tokenizer, loadSchemaToUse)
    val mobs = mutableListOf<Mob>()
    while (true) {
        try {
            mobs.add(mobLoader.load().build())
        } catch (e: EOFException) {
            break
        }
    }
    return mobs
}
