package kotlinmud.io

fun subcommandWithFreeForm(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.FREE_FORM)
}

fun subcommandDirectionNoExit(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.DIRECTION_WITH_NO_EXIT)
}

fun subcommand(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND)
}

fun subcommandPlayerMob(): List<Syntax> {
    return listOf(Syntax.COMMAND, Syntax.SUBCOMMAND, Syntax.PLAYER_MOB)
}
