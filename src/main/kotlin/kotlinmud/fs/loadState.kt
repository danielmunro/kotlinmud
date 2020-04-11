package kotlinmud.fs

import java.io.File
import kotlinmud.fs.loader.AreaLoader
import kotlinmud.world.Area

fun getAreaResourcesFromFS(isTest: Boolean = false): List<Area> {
    return if (File("state").exists() && !isTest) {
        println("state exists and not in test environment, loading")
        listOf(AreaLoader("state/bootstrap_world").load())
    } else if (isTest) {
        listOf(
            AreaLoader("test_areas/midgard").load(),
            AreaLoader("test_areas/midgard_castle").load(),
            AreaLoader("test_areas/woods").load()
        )
    } else {
        println("no state found, starting new")
        listOf(
            AreaLoader("bootstrap_world/midgard").load(),
            AreaLoader("bootstrap_world/midgard_castle").load(),
            AreaLoader("bootstrap_world/woods").load()
        )
    }
}
