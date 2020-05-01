package kotlinmud.fs

import java.io.File
import kotlinmud.fs.loader.Tokenizer

const val BUMP_LOAD_VERSION = false
const val BUMP_WRITE_VERSION = false

fun loadVersionState(isTest: Boolean = false): List<Int> {
    return if (File(VERSION_FILE).exists() && !isTest) {
        println("loading version state")
        val tokenizer = Tokenizer(File(VERSION_FILE).readText())
        val loadVersion = tokenizer.parseInt()
        val writeVersion = tokenizer.parseInt()
        listOf(
            loadVersion + if (BUMP_LOAD_VERSION) 1 else 0,
            writeVersion + if (BUMP_WRITE_VERSION) 1 else 0
        )
    } else {
        listOf(1, 1)
    }
}
