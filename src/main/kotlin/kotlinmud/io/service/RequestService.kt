package kotlinmud.io.service

import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.type.Disposition
import kotlinmud.room.model.Room

class RequestService(val mob: PlayerMob, val input: String) {
    val args: List<String>
    init {
        val setupArgs: MutableList<String> = mutableListOf()
        var buffer = ""
        var isOpen = false
        var escaped = false
        input.forEach {
            if (it == '\'' && !escaped) {
                if (isOpen) {
                    setupArgs.add(buffer)
                }
                buffer = ""
                isOpen = !isOpen
            } else if (it == '\\') {
                escaped = true
            } else if (it == ' ' && !isOpen && buffer != "") {
                setupArgs.add(buffer)
                buffer = ""
            } else if (isOpen || it != ' ') {
                buffer += it
                escaped = false
            }
        }
        if (buffer != "") {
            setupArgs.add(buffer)
        }
        args = setupArgs
    }

    fun getRoom(): Room {
        return mob.room
    }

    fun getCommand(): String {
        return args[0]
    }

    fun getSubject(): String {
        return if (args.size > 1) args[1] else ""
    }

    fun getModifier(): String {
        return if (args.size > 2) args[2] else ""
    }

    fun getDisposition(): Disposition {
        return mob.disposition
    }
}
