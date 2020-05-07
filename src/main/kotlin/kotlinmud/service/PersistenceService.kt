package kotlinmud.service

import java.io.File
import kotlinmud.fs.TIME_FILE
import kotlinmud.fs.VERSION_FILE
import kotlinmud.fs.loader.AreaLoader
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.world.Area
import kotlinmud.world.World
import org.slf4j.LoggerFactory

const val CURRENT_LOAD_SCHEMA_VERSION = 2
const val CURRENT_WRITE_SCHEMA_VERSION = 2

class PersistenceService(private val loadSchemaVersion: Int, private val writeSchemaVersion: Int = loadSchemaVersion) {
    private val logger = LoggerFactory.getLogger(PersistenceService::class.java)

    init {
        logger.info("load schema from file: {}, write: {}", loadSchemaVersion, writeSchemaVersion)
        logger.info("hardcoded overwrite: load: {}, write: {}", CURRENT_LOAD_SCHEMA_VERSION, CURRENT_WRITE_SCHEMA_VERSION)
    }

    fun writeVersionFile() {
        val file = File(VERSION_FILE)
        file.writeText("""#$CURRENT_LOAD_SCHEMA_VERSION
#$CURRENT_WRITE_SCHEMA_VERSION""")
    }

    fun writeTimeFile(timeService: TimeService) {
        logger.info("write time at {}", timeService.getTime())
        val file = File(TIME_FILE)
        file.writeText("#${timeService.getTime()}")
    }

    fun writeAreas(world: World) {
        logger.info("write areas :: write schema {}", CURRENT_WRITE_SCHEMA_VERSION)
        WorldSaver(world).save()
    }

    fun loadTimeFile(): Int {
        val file = File(TIME_FILE)
        return if (file.exists()) Tokenizer(file.readText()).parseInt() else 0
    }

    fun loadAreas(): List<Area> {
        return listOf(AreaLoader("state/bootstrap_world", CURRENT_LOAD_SCHEMA_VERSION).load())
    }
}
