package kotlinmud.helper

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

fun getAllDataFiles(): List<String> {
    return Files.list(Paths.get("./world")).map {
        File(it.toUri()).readText()
    }.toList()
}
