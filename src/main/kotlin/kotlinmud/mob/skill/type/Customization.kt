package kotlinmud.mob.skill.type

interface Customization {
    val name: String
    val points: Int
    val creationGroupType: CreationGroupType
    val helpText: String
}
