package kotlinmud.affect

fun affect(affectType: AffectType): AffectInstance {
    return AffectInstance(affectType)
}

fun affects(affectType: AffectType): MutableList<AffectInstance> {
    return mutableListOf(AffectInstance(affectType))
}
