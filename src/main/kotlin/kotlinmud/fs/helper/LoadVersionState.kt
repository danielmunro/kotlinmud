package kotlinmud.fs.helper

import kotlinmud.fs.factory.versionFile
import kotlinmud.fs.loader.Tokenizer

fun loadVersionState(isTest: Boolean = false): List<Int> {
    val file = versionFile()
    return if (file.exists() && !isTest) {
        val tokenizer = Tokenizer(file.readText())
        val loadVersion = tokenizer.parseInt()
        val writeVersion = tokenizer.parseInt()
        listOf(loadVersion, writeVersion)
    } else {
        listOf(1, 1)
    }
}
