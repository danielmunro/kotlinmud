package kotlinmud.service

import java.io.File
import kotlinmud.fs.TIME_FILE
import kotlinmud.fs.VERSION_FILE
import kotlinmud.fs.loader.Tokenizer

class PersistenceService(private val loadSchemaVersion: Int, private val writeSchemaVersion: Int = loadSchemaVersion) {
    init {
        println("persistence service, load schema version: $loadSchemaVersion, write schema version: $writeSchemaVersion")
    }

    fun writeVersionFile() {
        val file = File(VERSION_FILE)
        file.appendText("#$loadSchemaVersion")
        file.appendText("#$writeSchemaVersion")
    }

    fun writeTimeFile(timeService: TimeService) {
        val file = File(TIME_FILE)
        file.writeText("#${timeService.getTime()}")
    }

    fun loadTimeFile(): Int {
        return Tokenizer(File(TIME_FILE).readText()).parseInt()
    }
}
