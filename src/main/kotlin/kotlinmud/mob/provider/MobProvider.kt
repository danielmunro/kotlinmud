package kotlinmud.mob.provider

import java.io.EOFException
import kotlinmud.fs.factory.playerMobFile
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.mob.loader.MobLoader
import kotlinmud.mob.model.Mob

fun loadMobs(loadSchemaToUse: Int): MutableList<Mob> {
    val file = playerMobFile()
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
