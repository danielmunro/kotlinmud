package kotlinmud.fs.factory

import java.io.File
import kotlinmud.fs.constant.MOB_CARD_FILE
import kotlinmud.fs.constant.PLAYER_FILE
import kotlinmud.fs.constant.PLAYER_MOBS_FILE
import kotlinmud.fs.constant.TIME_FILE
import kotlinmud.fs.constant.VERSION_FILE

fun versionFile(): File {
    return File(VERSION_FILE)
}

fun timeFile(): File {
    return File(TIME_FILE)
}

fun playerFile(): File {
    return File(PLAYER_FILE)
}

fun playerMobFile(): File {
    return File(PLAYER_MOBS_FILE)
}

fun mobCardFile(): File {
    return File(MOB_CARD_FILE)
}
