package kotlinmud.loader.loader

import kotlinmud.loader.model.Model

interface Loader {
    var props: Map<String, String>

    fun load(): Model

    fun intAttr(name: String, default: Int = 0): Int {
        return props[name]?.toInt() ?: default
    }

    fun strAttr(name: String, default: String = ""): String {
        return props[name] ?: default
    }
}
