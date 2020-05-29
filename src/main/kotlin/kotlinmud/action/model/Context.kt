package kotlinmud.action.model

import kotlinmud.action.type.Status
import kotlinmud.io.Syntax

data class Context<T>(val syntax: Syntax, val status: Status, val result: T)
