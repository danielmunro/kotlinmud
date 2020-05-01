package kotlinmud.service

import java.io.File
import kotlinmud.fs.TIME_FILE
import kotlinmud.fs.VERSION_FILE
import kotlinmud.fs.loader.AreaLoader
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.world.Area
import kotlinmud.world.World

const val CURRENT_LOAD_SCHEMA_VERSION = 1
const val CURRENT_WRITE_SCHEMA_VERSION = 2

class PersistenceService(private val loadSchemaVersion: Int, private val writeSchemaVersion: Int = loadSchemaVersion) {
    init {
        println("persistence service, load schema version: $loadSchemaVersion, write schema version: $writeSchemaVersion")
    }

    fun writeVersionFile() {
        val file = File(VERSION_FILE)
        file.writeText("""#$CURRENT_LOAD_SCHEMA_VERSION
#$CURRENT_WRITE_SCHEMA_VERSION""")
    }

    fun writeTimeFile(timeService: TimeService) {
        val file = File(TIME_FILE)
        file.writeText("#${timeService.getTime()}")
    }

    fun writeAreas(world: World) {
        WorldSaver(world).save()
    }

    fun loadTimeFile(): Int {
        return Tokenizer(File(TIME_FILE).readText()).parseInt()
    }

    fun loadAreas(): List<Area> {
        return listOf(AreaLoader("state/bootstrap_world", CURRENT_LOAD_SCHEMA_VERSION).load())
    }
}
