package kotlinmud.fs

import java.io.File
import kotlinmud.fs.loader.Tokenizer

fun loadVersionState(isTest: Boolean = false): List<Int> {
    return if (File(VERSION_FILE).exists() && !isTest) {
        println("loading version state")
        val tokenizer = Tokenizer(File(VERSION_FILE).readText())
        val loadVersion = tokenizer.parseInt()
        val writeVersion = tokenizer.parseInt()
        listOf(loadVersion, writeVersion)
    } else {
        listOf(1, 1)
    }
}
