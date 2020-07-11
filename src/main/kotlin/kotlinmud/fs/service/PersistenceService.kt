package kotlinmud.fs.service

import kotlinmud.fs.constant.CURRENT_LOAD_SCHEMA_VERSION
import kotlinmud.fs.constant.CURRENT_WRITE_SCHEMA_VERSION
import kotlinmud.fs.factory.timeFile
import kotlinmud.fs.factory.versionFile
import kotlinmud.fs.loader.Tokenizer
import kotlinmud.fs.saver.WorldSaver
import kotlinmud.helper.logger
import kotlinmud.service.TimeService
import kotlinmud.world.model.World

class PersistenceService {
    private val logger = logger(this)

    fun writeVersionFile() {
        logger.info("version file :: write schema {}, load schema: {}",
            CURRENT_WRITE_SCHEMA_VERSION,
            CURRENT_LOAD_SCHEMA_VERSION
        )
        versionFile().writeText("""#$CURRENT_LOAD_SCHEMA_VERSION
#$CURRENT_WRITE_SCHEMA_VERSION""")
    }

    fun writeTimeFile(timeService: TimeService) {
        logger.info("time file :: {}", timeService.getTime())
        timeFile().writeText("#${timeService.getTime()}")
    }

    fun writeWorld(world: World) {
        logger.info("areas file :: write schema {}",
            CURRENT_WRITE_SCHEMA_VERSION
        )
        WorldSaver(world).save()
    }

    fun loadTimeFile(): Int {
        val file = timeFile()
        return if (file.exists()) Tokenizer(file.readText()).parseInt() else 0
    }
}
