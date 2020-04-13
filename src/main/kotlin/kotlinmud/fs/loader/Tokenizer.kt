package kotlinmud.fs.loader

import java.io.EOFException

class Tokenizer(value: String) {
    private val raw = removeComments(value).split(":", " ", "\n")
    private var i = 0

    private fun getNext(): String {
        if (i == raw.size) {
            throw EOFException()
        }
        val value = raw[i]
        i++
        return value.trim()
    }

    private fun getNextNonEmpty(): String {
        val next = getNext()
        if (next == "") {
            return getNextNonEmpty()
        }
        return next
    }

    fun parseInt(): Int {
        val next = getNext()
        if (next == "") {
            return parseInt()
        }
        return next.substring(1).toInt()
    }

    fun parseString(): String {
        var buf = getNext()
        var toReturn = ""
        while (!buf.contains("~")) {
            toReturn += if (buf == "") "\n" else "$buf "
            buf = getNext()
        }
        toReturn += buf.substring(0, buf.length - 1)
        return toReturn
    }

    fun parseProperties(): Map<String, String> {
        var key = getNextNonEmpty()
        if (key == "~") {
            return mapOf()
        }
        var value = getNextNonEmpty()
        val map = mutableMapOf<String, String>()
        while (!value.contains("~")) {
            map += Pair(key.trim(':'), value.trim(',', '~'))
            key = getNextNonEmpty()
            value = getNextNonEmpty()
        }
        map += Pair(key.trim(':'), value.trim(',', '~'))
        return map
    }
}

fun removeComments(value: String): String {
    return value
        .split("\n")
        .filter { !it.startsWith("//") }
        .joinToString("\n") { it }
}
