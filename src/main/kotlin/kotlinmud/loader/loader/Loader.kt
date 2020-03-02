package kotlinmud.loader.loader

import kotlinmud.loader.model.Model

interface Loader {
    fun load(): Model
}
