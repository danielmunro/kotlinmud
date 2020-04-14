package kotlinmud.fs.loader.area.loader

interface Loader {
    var props: Map<String, String>

    fun load(): Any

    fun intAttr(name: String, default: Int = 0): Int {
        return props[name]?.toInt() ?: default
    }

    fun strAttr(name: String, default: String = ""): String {
        return props[name] ?: default
    }
}
