package kotlinmud.action.service

import kotlinmud.action.exception.InvokeException
import kotlinmud.action.model.ActionContextList
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createSocialEvent
import kotlinmud.event.factory.createTillEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.Noun
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
import kotlinmud.item.type.ItemCanonicalId
import kotlinmud.item.type.Recipe
import kotlinmud.mob.model.Mob
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.social.Social
import kotlinmud.quest.service.QuestService
import kotlinmud.quest.type.Quest
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.Room
import kotlinmud.room.service.RoomService
import kotlinmud.room.type.Direction
import kotlinmud.weather.service.WeatherService
import kotlinmud.weather.type.Weather
import org.jetbrains.exposed.sql.transactions.transaction

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
) {
    private val craftingService = CraftingService(itemService)
    val recipes = createRecipeList()

    fun craft(recipe: Recipe): List<Item> {
        return craftingService.craft(recipe, request.mob)
    }

    fun harvest(resource: ResourceDAO): List<Item> {
        return craftingService.harvest(resource, request.mob)
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getMob(): Mob {
        return request.mob
    }

    fun getMobCard(): MobCardDAO {
        return request.mob.mobCard!!
    }

    fun getAffects(): List<Affect> {
        return getMob().affects
    }

    fun getLevel(): Int {
        return getMob().level
    }

    fun getRoom(): Room {
        return request.getRoom()
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

    suspend fun moveMob(room: Room, direction: Direction) {
        mobService.moveMob(request.mob, room, direction)
    }

    fun createSpellInvokeResponse(target: Noun, affect: AffectInterface, delay: Int = 1): Response {
        return createOkResponse(
            affect.messageFromInstantiation(request.mob, target),
            delay
        )
    }

    fun createOkResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.OK, actionContextList, message, delay)
    }

    fun createErrorResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.ERROR, actionContextList, message, delay)
    }

    suspend fun createFight() {
        val target: Mob = get(Syntax.MOB_IN_ROOM)
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

    fun getClients(): Clients {
        return serverService.getClients()
    }

    fun getItemGroupsFor(mob: Mob): Map<ItemCanonicalId?, List<Item>> {
        return itemService.getItemGroups(mob)
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
//        val room = request.getRoom()
//        return "${getRoomName(weatherService.getTemperature(), room.biome)}\n${getRoomDescription(room, weatherService.getWeather())}"
    }

    fun train(attribute: Attribute) {
        mobService.train(getMobCard(), attribute)
    }

    fun practice(skillType: SkillType) {
        mobService.practice(request.mob, skillType)
    }

    fun setDisposition(disposition: Disposition) {
        transaction { getMob().disposition = disposition }
    }

    fun getAcceptableQuests(): List<Quest> {
        return questService.getAcceptableQuestsForMob(request.mob)
    }

    fun getAcceptedQuests(): List<Quest> {
        return questService.getAcceptedQuestsForMob(request.mob)
    }

    fun getSubmittableQuests(): List<Quest> {
        return questService.getSubmittableQuestsForMob(request.mob)
    }

    fun submitQuest(quest: Quest) {
        questService.submit(getMobCard(), quest)
    }

    fun acceptQuest(quest: Quest) {
        questService.accept(getMobCard(), quest)
    }

    fun abandonQuest(quest: Quest) {
        questService.abandon(getMobCard(), quest)
    }

    fun getQuestLog(): List<Quest> {
        return questService.getLog(getMobCard())
    }
}
