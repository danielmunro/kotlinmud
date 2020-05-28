package kotlinmud.action

import kotlinmud.affect.AffectInstance
import kotlinmud.event.Event
import kotlinmud.event.EventService
import kotlinmud.event.EventType
import kotlinmud.event.event.FightStartedEvent
import kotlinmud.event.event.SocialEvent
import kotlinmud.io.IOStatus
import kotlinmud.io.Message
import kotlinmud.io.NIOClients
import kotlinmud.io.NIOServer
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.item.HasInventory
import kotlinmud.item.Item
import kotlinmud.item.ItemService
import kotlinmud.item.Recipe
import kotlinmud.item.createRecipeList
import kotlinmud.mob.Mob
import kotlinmud.mob.MobService
import kotlinmud.mob.fight.Fight
import kotlinmud.player.PlayerService
import kotlinmud.player.model.MobCard
import kotlinmud.player.social.Social
import kotlinmud.service.CraftingService
import kotlinmud.service.WeatherService
import kotlinmud.weather.Weather
import kotlinmud.world.ResourceType
import kotlinmud.world.room.Direction
import kotlinmud.world.room.NewRoom
import kotlinmud.world.room.RegenLevel
import kotlinmud.world.room.Room
import kotlinmud.world.room.exit.Exit

class ActionContextService(
    private val mobService: MobService,
    private val playerService: PlayerService,
    private val itemService: ItemService,
    private val eventService: EventService,
    private val weatherService: WeatherService,
    private val actionContextList: ActionContextList,
    private val server: NIOServer,
    private val request: Request
) {
    private val craftingService = CraftingService(itemService, createRecipeList())

    fun craft(recipe: Recipe): List<Item> {
        return craftingService.craft(recipe, request.mob)
    }

    fun harvest(resourceType: ResourceType): Item {
        return craftingService.harvest(resourceType, request.room, request.mob)
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getMob(): Mob {
        return request.mob
    }

    fun getMobCard(): MobCard {
        return playerService.findMobCardByName(getMob().name)!!
    }

    fun getAffects(): List<AffectInstance> {
        return getMob().affects
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

    fun getRoom(): Room {
        return request.room
    }

    fun getExits(): List<Exit> {
        return request.room.exits
    }

    fun getRecall(): Room {
        return mobService.getStartRoom()
    }

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(): List<Mob> {
        return mobService.getMobsForRoom(getRoom())
    }

    fun moveMob(room: Room, direction: Direction) {
        mobService.moveMob(getMob(), room, direction)
    }

    fun putMobInRoom(room: Room) {
        mobService.putMobInRoom(getMob(), room)
    }

    fun createOkResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.OK, actionContextList, message, delay)
    }

    fun createErrorResponse(message: Message, delay: Int = 0): Response {
        return Response(IOStatus.ERROR, actionContextList, message, delay)
    }

    fun createFight() {
        val target: Mob = get(Syntax.MOB_IN_ROOM)
        val fight = Fight(getMob(), target)
        mobService.addFight(fight)
        eventService.publish(Event(EventType.FIGHT_STARTED, FightStartedEvent(fight, getMob(), target)))
    }

    fun endFight() {
        mobService.endFightFor(getMob())
    }

    fun publishSocial(social: Social) {
        eventService.publish(Event(EventType.SOCIAL, SocialEvent(social)))
    }

    fun getClients(): NIOClients {
        return server.getClients()
    }

    fun getItemsFor(hasInventory: HasInventory): List<Item> {
        return itemService.findAllByOwner(hasInventory)
    }

    fun getItemGroupsFor(mob: Mob): Map<Int, List<Item>> {
        return itemService.getItemGroups(mob)
    }

    fun changeItemOwner(item: Item, hasInventory: HasInventory) {
        itemService.changeItemOwner(item, hasInventory)
    }

    fun destroy(item: Item) {
        itemService.destroy(item)
    }

    fun createNewRoom(name: String): NewRoom {
        val newRoom = mobService.createNewRoom(getMob())
        newRoom.roomBuilder
            .name(name)
            .area(getRoom().area)
            .description("A new room has been created")
            .isIndoor(getRoom().isIndoor)
            .owner(getRoom().owner)
            .regen(RegenLevel.NORMAL)
        return newRoom
    }

    fun buildRoom(mob: Mob, direction: Direction): Room {
        return mobService.buildRoom(mob, direction)
    }

    fun getNewRoom(): NewRoom? {
        return mobService.getNewRoom(getMob())
    }
}
