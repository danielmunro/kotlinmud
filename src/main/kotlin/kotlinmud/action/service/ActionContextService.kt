package kotlinmud.action.service

import kotlinmud.action.model.ActionContextList
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.biome.type.ResourceType
import kotlinmud.event.impl.Event
import kotlinmud.event.impl.FightStartedEvent
import kotlinmud.event.impl.SocialEvent
import kotlinmud.event.service.EventService
import kotlinmud.event.type.EventType
import kotlinmud.io.model.Message
import kotlinmud.io.model.Request
import kotlinmud.io.model.Response
import kotlinmud.io.service.ServerService
import kotlinmud.io.type.Clients
import kotlinmud.io.type.IOStatus
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.item.helper.createRecipeList
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Recipe
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.service.MobService
import kotlinmud.player.model.MobCard
import kotlinmud.player.service.PlayerService
import kotlinmud.player.social.Social
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.NewRoom
import kotlinmud.room.type.Direction
import kotlinmud.room.type.RegenLevel
import kotlinmud.service.WeatherService
import kotlinmud.weather.Weather
import org.jetbrains.exposed.dao.EntityID

class ActionContextService(
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val weatherService: WeatherService,
    private val actionContextList: ActionContextList,
    private val serverService: ServerService,
    private val request: Request
) {
    private val craftingService = CraftingService(
        itemService,
        createRecipeList()
    )

    fun craft(recipe: Recipe): List<ItemDAO> {
        return craftingService.craft(recipe, request.mob)
    }

    fun harvest(resourceType: ResourceType): List<ItemDAO> {
        return craftingService.harvest(resourceType, request.room, request.mob)
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getMob(): MobDAO {
        return request.mob
    }

    fun getMobCard(): MobCard {
        return playerService.findMobCardByName(getMob().name)!!
    }

    fun getAffects(): List<AffectDAO> {
        return getMob().affects.toList()
    }

    fun getLevel(): Int {
        return getMob().level
    }

    fun addGold(amount: Int) {
        getMob().gold += amount
    }

    fun deductGold(amount: Int) {
        getMob().gold -= amount
    }

    fun getRoom(): RoomDAO {
        return request.room
    }

    fun getExits(): Map<Direction, RoomDAO> {
        return request.room.getAllExits()
    }

    fun getRecall(): RoomDAO {
        return mobService.getStartRoom()
    }

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(): List<MobDAO> {
        return mobService.getMobsForRoom(getRoom())
    }

    fun moveMob(room: RoomDAO, direction: Direction) {
        mobService.moveMob(getMob(), room, direction)
    }

    fun putMobInRoom(room: RoomDAO) {
        mobService.putMobInRoom(getMob(), room)
    }

    fun createOkResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.OK, actionContextList, message, delay)
    }

    fun createErrorResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.ERROR, actionContextList, message, delay)
    }

    fun createFight() {
        val target: MobDAO = get(Syntax.MOB_IN_ROOM)
        val fight = Fight(getMob(), target)
        mobService.addFight(fight)
        eventService.publish(
            Event(
                EventType.FIGHT_STARTED,
                FightStartedEvent(fight, getMob(), target)
            )
        )
    }

    fun endFight() {
        mobService.endFightFor(getMob())
    }

    fun publishSocial(social: Social) {
        eventService.publish(Event(EventType.SOCIAL, SocialEvent(social)))
    }

    fun getClients(): Clients {
        return serverService.getClients()
    }

    fun getItemsFor(hasInventory: HasInventory): List<ItemDAO> {
        return itemService.findAllByOwner(hasInventory)
    }

    fun getItemGroupsFor(mob: MobDAO): Map<EntityID<Int>, List<ItemDAO>> {
        return itemService.getItemGroups(mob)
    }

    fun giveItemToMob(item: ItemDAO, mob: MobDAO) {
        itemService.giveItemToMob(item, mob)
    }

    fun putItemInRoom(item: ItemDAO, room: RoomDAO) {
        itemService.putItemInRoom(item, room)
    }

    fun putItemInContainer(item: ItemDAO, container: ItemDAO) {
        itemService.putItemInContainer(item, container)
    }

    fun destroy(item: ItemDAO) {
        itemService.destroy(item)
    }

    fun createNewRoom(name: String): NewRoom {
        val newRoom = mobService.createNewRoom(getMob())
        val room = newRoom.room
        room.name = name
        room.area = getRoom().area
        room.description = "A new room has been created"
        room.isIndoor = getRoom().isIndoor
        room.owner = getRoom().owner
        room.regenLevel = RegenLevel.NORMAL
        return newRoom
    }

    fun buildRoom(mob: MobDAO, direction: Direction): RoomDAO {
        return mobService.buildRoom(mob, direction)
    }

    fun getNewRoom(): NewRoom? {
        return mobService.getNewRoom(getMob())
    }
}
