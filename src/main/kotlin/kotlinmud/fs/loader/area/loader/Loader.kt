package kotlinmud.fs.loader.area.loader

interface Loader {
    fun load(): Any
}

fun intAttr(value: String?, default: Int = 0): Int {
    return value?.toInt() ?: default
}

fun strAttr(value: String?, default: String = ""): String {
    return value ?: default
}
