package kotlinmud.action

import kotlinmud.io.Syntax

class Context<T>(val syntax: Syntax, val status: Status, val result: T)
