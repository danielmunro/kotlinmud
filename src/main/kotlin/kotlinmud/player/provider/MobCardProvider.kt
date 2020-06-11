package kotlinmud.player.provider

import java.io.EOFException
import kotlinmud.fs.factory.mobCardFile
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.player.loader.MobCardLoader
import kotlinmud.player.model.MobCard

fun loadMobCards(): MutableList<MobCard> {
    val file = mobCardFile()
    if (!file.exists()) {
        return mutableListOf()
    }
    val tokenizer = Tokenizer(file.readText())
    val mobCardLoader = MobCardLoader(tokenizer)
    val mobCards = mutableListOf<MobCard>()
    while (true) {
        try {
            mobCards.add(mobCardLoader.load())
        } catch (e: EOFException) {
            break
        }
    }
    return mobCards
}
