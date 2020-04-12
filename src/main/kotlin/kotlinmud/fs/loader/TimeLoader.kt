package kotlinmud.fs.loader

import java.io.File

class TimeLoader {
    private val tokenizer: Tokenizer = Tokenizer(File("./state/time.txt").readText())

    fun load(): Int {
        return tokenizer.parseInt()
    }
}
