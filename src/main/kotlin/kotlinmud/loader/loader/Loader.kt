package kotlinmud.loader.loader

import kotlinmud.loader.model.Model

interface Loader {
    var props: Map<String, String>

    fun load(): Model

    fun intAttr(name: String): Int {
        return props[name]?.toInt() ?: 0
    }

    fun strAttr(name: String, default: String = ""): String {
        return props[name] ?: default
    }
}
