package kotlinmud.fs.saver

import kotlinmud.generator.type.Layer

fun layerToString(layer: Layer): String {
    return layer.joinToString("\n") {
        it.joinToString(" ") { value ->
            if (value > 9) value.toString() else " $value"
        }
    }
}
