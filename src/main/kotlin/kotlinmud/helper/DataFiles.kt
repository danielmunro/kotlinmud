package kotlinmud.helper

import java.io.File
import kotlin.streams.toList

fun getAllDataFiles(): List<String> {
    println("get all data files")
    return File("world").walkTopDown().mapNotNull {
        val file = File(it.toURI())
        println(file.toURI())
        if (file.isFile()) {
            return@mapNotNull file.readText()
        }
        return@mapNotNull null
    }.toList()
//    return Files.list(Paths.get("./world")).map {
//        File(it.toUri()).readText()
//    }.toList()
}
