package kotlinmud.player.provider

import java.io.EOFException
import java.io.File
import kotlinmud.fs.MOB_CARD_FILE
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.player.loader.MobCardLoader
import kotlinmud.player.model.MobCard

fun loadMobCards(): MutableList<MobCard> {
    val file = File(MOB_CARD_FILE)
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
