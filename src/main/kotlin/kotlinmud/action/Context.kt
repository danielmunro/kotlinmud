package kotlinmud.action

import kotlinmud.io.Syntax

class Context<A>(val syntax: Syntax, val status: Status, val result: A) {
}
