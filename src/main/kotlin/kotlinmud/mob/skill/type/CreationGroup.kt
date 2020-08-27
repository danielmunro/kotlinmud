package kotlinmud.mob.skill.type

interface CreationGroup {
    val name: String
    val skills: List<SkillAction>
    val points: Int
}
