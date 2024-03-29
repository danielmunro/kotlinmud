package kotlinmud.action.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.model.ActionContextList
import kotlinmud.affect.model.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.biome.type.ResourceType
import kotlinmud.event.factory.createRebootEvent
import kotlinmud.event.factory.createSocialEvent
import kotlinmud.event.factory.createTillEvent
import kotlinmud.event.service.EventService
import kotlinmud.io.model.Message
import kotlinmud.io.model.Response
import kotlinmud.io.service.RequestService
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.Clients
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.model.Item
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.helper.getLearningDifficultyPracticeAmount
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.Skill
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Disposition
import kotlinmud.persistence.dumper.AreaDumperService
import kotlinmud.persistence.model.MobModel
import kotlinmud.persistence.model.RoomModel
import kotlinmud.player.social.Social
import kotlinmud.quest.model.Quest
import kotlinmud.quest.service.QuestService
import kotlinmud.room.builder.RoomBuilder
import kotlinmud.room.model.Area
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Direction
import kotlinmud.room.type.getReverseDirection
import kotlinmud.time.service.TimeService
import kotlinmud.weather.service.WeatherService
import kotlinmud.weather.type.Weather
import kotlin.math.roundToInt

class ActionContextService(
    private val mobService: MobService,
    private val itemService: ItemService,
    private val roomService: RoomService,
    private val eventService: EventService,
    private val weatherService: WeatherService,
    private val actionContextList: ActionContextList,
    private val serverService: ServerService,
    private val request: RequestService,
    private val questService: QuestService,
    private val timeService: TimeService,
) {
    private val craftingService = CraftingService(itemService)
    val recipes = createRecipeList()
    val skills = createSkillList()

    fun craft(recipe: Recipe): List<Item> {
        return craftingService.craft(recipe, request.mob)
    }

    fun harvest(resource: ResourceType): List<Item> {
        return craftingService.harvest(resource, request.mob)
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getPlayerMobsInArea(): List<PlayerMob> {
        return mobService.findPlayerMobsInArea(getRoom().area)
    }

    fun getMob(): PlayerMob {
        return request.mob
    }

    fun getAffects(): List<Affect> {
        return getMob().affects
    }

    fun getLevel(): Int {
        return getMob().level
    }

    fun getAreas(): List<Area> {
        return roomService.getAllAreas()
    }

    fun addArea(area: Area) {
        roomService.addArea(area)
    }

    fun getRoom(): Room {
        return request.getRoom()
    }

    fun addRoom(direction: Direction) {
        val source = request.getRoom()
        val sourceModel = roomService.getModel(source.id)!! as RoomModel
        val destinationModel = RoomModel(
            roomService.getNextAutoId(),
            sourceModel.name,
            sourceModel.description,
            sourceModel.keywords,
            sourceModel.area
        )
        val destination = RoomBuilder(roomService).also {
            it.id = destinationModel.id
            it.name = source.name
            it.brief = source.brief
            it.description = source.description
            it.area = source.area
            it.elevation = source.elevation
        }.build()
        roomService.addModel(destinationModel)
        setDirectionOnModel(sourceModel, destinationModel, direction)
        setDirectionOnRoom(source, destination, direction)
    }

    fun flush() {
        AreaDumperService(roomService, mobService, itemService).dump()
    }

    fun getExits(): Map<Direction, Room> {
        return request.getRoom().getAllExits()
    }

    fun getRecall(): Room {
        return roomService.getStartRoom()
    }

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(): List<Mob> {
        return mobService.findMobsInRoom(getRoom())
    }

    fun createMobModel(name: String): MobModel {
        return MobModel(
            mobService.getNextAutoId(),
            name,
            "a new mob, fresh from the factory",
            "a new mob is here. They still have the new mob smell.",
            getRoom().area,
            mapOf(),
            listOf(),
        ).also {
            mobService.addModel(it)
        }
    }

    suspend fun moveMob(room: Room, direction: Direction) {
        mobService.moveMob(request.mob, room, direction)
    }

    fun createOkResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.OK, actionContextList, message, delay)
    }

    fun createErrorResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.ERROR, actionContextList, message, delay)
    }

    suspend fun createFight(target: Mob) {
        val fight = mobService.addFight(request.mob, target)
        eventService.publish(fight.createFightStartedEvent())
    }

    fun flee() {
        mobService.flee(request.mob)
    }

    suspend fun publishSocial(social: Social) {
        eventService.publish(createSocialEvent(social))
    }

    suspend fun publishTillEvent(room: Room) {
        eventService.publish(createTillEvent(room))
    }

    suspend fun publishReboot() {
        eventService.publish(createRebootEvent())
    }

    fun getClients(): Clients {
        return serverService.getClients()
    }

    fun destroyItem(item: Item) {
        itemService.remove(item)
    }

    fun findMobItem(predicate: (Item) -> Boolean): Item? {
        return request.mob.items.find(predicate)
    }

    fun findArea(partial: String): Area? {
        return roomService.findArea(partial)
    }

    fun getItemGroupsFor(mob: Mob): Map<String, List<Item>> {
        return mob.items.groupBy { it.name }
    }

    fun giveItemToMob(item: Item, mob: Mob) {
        if (mob.items.count() >= mob.maxItems || mob.items.fold(0.0, { acc: Double, it: Item -> acc + it.weight }) + item.weight > mob.maxWeight) {
            throw InvokeException("you cannot carry any more.")
        }
        mob.items.add(item)
    }

    fun putItemInContainer(item: Item, container: Item) {
        itemService.putItemInContainer(item, container)
    }

    fun getDynamicRoomDescription(): String {
        val room = request.getRoom()
        return "${room.name}\n${room.description}\n"
    }

    fun setDisposition(disposition: Disposition) {
        getMob().disposition = disposition
    }

    fun getAcceptableQuests(): List<Quest> {
        return questService.getAcceptableQuestsForMob(request.mob)
    }

    fun submitQuest(quest: Quest) {
        questService.submit(getMob(), quest)
    }

    fun acceptQuest(quest: Quest) {
        questService.accept(getMob(), quest)
    }

    fun abandonQuest(quest: Quest) {
        questService.abandon(getMob(), quest)
    }

    fun getQuestLog(): List<Quest> {
        return questService.getLog(getMob())
    }

    fun getDate(): String {
        return timeService.getDate()
    }

    fun calculatePracticeGain(mob: Mob, type: SkillType): Int {
        return with(
            1 + getLearningDifficultyPracticeAmount(
                getSkillDifficultyForSpecialization(type, mob.specialization?.type ?: SpecializationType.NONE)
            )
        ) {
            (Math.random() * this + mob.calc(Attribute.INT) / 5).roundToInt()
        }
    }

    fun setRoomArea(roomId: Int, area: Area) {
        roomService.setRoomArea(roomId, area)
    }

    fun setRoomBrief(roomId: Int, brief: String) {
        roomService.setRoomBrief(roomId, brief)
    }

    fun addToRoomDescription(roomId: Int, description: String) {
        roomService.addToRoomDescription(roomId, description)
    }

    fun changeRoomDescription(roomId: Int, change: String, lineNumber: Int) {
        roomService.changeRoomDescription(roomId, change, lineNumber)
    }

    fun removeRoomDescription(roomId: Int, lineNumber: Int) {
        roomService.removeRoomDescription(roomId, lineNumber)
    }

    private fun setDirectionOnRoom(source: Room, destination: Room, direction: Direction) {
        when (direction) {
            Direction.NORTH -> {
                source.north = destination
                destination.south = source
            }
            Direction.SOUTH -> {
                source.south = destination
                destination.north = source
            }
            Direction.EAST -> {
                source.east = destination
                destination.west = source
            }
            Direction.WEST -> {
                source.west = destination
                destination.east = source
            }
            Direction.UP -> {
                source.up = destination
                destination.down = source
            }
            Direction.DOWN -> {
                source.down = destination
                destination.up = source
            }
        }
    }

    private fun setDirectionOnModel(source: RoomModel, destination: RoomModel, direction: Direction) {
        val reverse = getReverseDirection(direction)
        source.keywords[direction.value] = destination.id.toString()
        destination.keywords[reverse.value] = source.id.toString()
    }

    private fun getSkillDifficultyForSpecialization(type: SkillType, specialization: SpecializationType?): LearningDifficulty {
        return findSkillByType(type).difficulty[specialization] ?: LearningDifficulty.VERY_HARD
    }

    private fun findSkillByType(type: SkillType): Skill {
        return skills.find { it.type == type }!!
    }
}
