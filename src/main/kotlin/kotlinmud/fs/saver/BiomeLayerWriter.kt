package kotlinmud.fs.saver

import kotlinmud.world.generation.Layer

fun outputBiomeLayer(layer: Layer): String {
    return layer.joinToString("\n") {
        it.joinToString(" ")
    }
}
