package kotlinmud.mob.skill.type

interface SpellGroup {
    val name: String
    val spells: List<Skill>
}
