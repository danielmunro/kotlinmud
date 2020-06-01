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

const val CURRENT_LOAD_SCHEMA_VERSION = 9
const val CURRENT_WRITE_SCHEMA_VERSION = 10

class PersistenceService(
    private val previousLoadSchemaVersion: Int,
    private val previousWriteSchemaVersion: Int
) {
    private val logger = LoggerFactory.getLogger(PersistenceService::class.java)
    val loadSchemaToUse = CURRENT_LOAD_SCHEMA_VERSION.coerceAtLeast(previousWriteSchemaVersion)

    init {
        logger.info("previous load schema: {}, write schema: {}", previousLoadSchemaVersion, previousWriteSchemaVersion)
        logger.info("hardcoded overrides :: load schema: {}, write schema: {}", CURRENT_LOAD_SCHEMA_VERSION, CURRENT_WRITE_SCHEMA_VERSION)
    }

    fun writeVersionFile() {
        logger.info("version file :: write schema {}, load schema: {}", CURRENT_WRITE_SCHEMA_VERSION, CURRENT_LOAD_SCHEMA_VERSION)
        val file = File(VERSION_FILE)
        file.writeText("""#$CURRENT_LOAD_SCHEMA_VERSION
#$CURRENT_WRITE_SCHEMA_VERSION""")
    }

    fun writeTimeFile(timeService: TimeService) {
        logger.info("time file :: {}", timeService.getTime())
        val file = File(TIME_FILE)
        file.writeText("#${timeService.getTime()}")
    }

    fun writeAreas(world: World) {
        logger.info("areas file :: write schema {}", CURRENT_WRITE_SCHEMA_VERSION)
        WorldSaver(world).save()
    }

    fun loadTimeFile(): Int {
        val file = File(TIME_FILE)
        return if (file.exists()) Tokenizer(file.readText()).parseInt() else 0
    }

    fun loadAreas(isTest: Boolean): List<Area> {
        return if (File("state").exists() && !isTest) {
            getLiveAreaData()
        } else if (isTest) {
            getTestAreaData()
        } else {
            getBootstrapAreaData()
        }
    }

    private fun getLiveAreaData(): List<Area> {
        return listOf(AreaLoader("state/bootstrap_world", loadSchemaToUse).load())
    }

    private fun getTestAreaData(): List<Area> {
        return listOf(
            AreaLoader("test_areas/midgard", loadSchemaToUse).load(),
            AreaLoader("test_areas/midgard_castle", loadSchemaToUse).load(),
            AreaLoader("test_areas/woods", loadSchemaToUse).load()
        )
    }

    private fun getBootstrapAreaData(): List<Area> {
        return listOf(
            AreaLoader("bootstrap_world/midgard", loadSchemaToUse).load(),
            AreaLoader("bootstrap_world/midgard_castle", loadSchemaToUse).load(),
            AreaLoader("bootstrap_world/woods", loadSchemaToUse).load()
        )
    }
}
