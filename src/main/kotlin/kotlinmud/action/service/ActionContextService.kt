package kotlinmud.action.service

import kotlinmud.action.model.ActionContextList
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.biome.type.BiomeType
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
import kotlinmud.item.service.ItemService
import kotlinmud.item.type.HasInventory
import kotlinmud.item.type.Recipe
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.Fight
import kotlinmud.mob.service.MobService
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.player.social.Social
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.model.NewRoom
import kotlinmud.room.type.Direction
import kotlinmud.room.type.RegenLevel
import kotlinmud.service.WeatherService
import kotlinmud.weather.Temperature
import kotlinmud.weather.Weather
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.transactions.transaction

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
        itemService
    )

    companion object {
        fun getDirectionString(direction: Direction): String {
            return when (direction) {
                Direction.DOWN -> "below you"
                Direction.UP -> "above you"
                Direction.EAST -> "to the east"
                Direction.WEST -> "to the west"
                Direction.NORTH -> "to the north"
                Direction.SOUTH -> "to the south"
            }
        }
    }

    fun craft(recipe: Recipe): List<ItemDAO> {
        return craftingService.craft(recipe, request.mob)
    }

    fun harvest(resource: ResourceDAO): List<ItemDAO> {
        return craftingService.harvest(resource, request.mob)
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getMob(): MobDAO {
        return request.mob
    }

    fun getMobCard(): MobCardDAO {
        return playerService.findMobCardByName(getMob().name)!!
    }

    fun getAffects(): List<AffectDAO> {
        return transaction { getMob().affects.toList() }
    }

    fun getLevel(): Int {
        return getMob().level
    }

    fun addGold(amount: Int) {
        transaction { getMob().gold += amount }
    }

    fun deductGold(amount: Int) {
        transaction { getMob().gold -= amount }
    }

    fun transferGold(src: MobDAO, dst: MobDAO, amount: Int) {
        mobService.transferGold(src, dst, amount)
    }

    fun getRoom(): RoomDAO {
        return request.room
    }

    fun getExits(): Map<Direction, RoomDAO> {
        return request.room.getAllExits()
    }

    fun getRecall(): RoomDAO {
        return transaction {
            if (request.mob.isNpc) mobService.getStartRoom() else request.mob.mobCard!!.respawnRoom
        }
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
        transaction { getMob().room = room }
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

    fun flee() {
        mobService.flee(getMob())
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
        transaction { item.delete() }
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

    fun getDynamicRoomDescription(): String {
        return "${getRoomName()}\n${getRoomDescription()}"
    }

    private fun getRoomName(): String {
        return "A ${getTemperatureLabel()} ${getBiomeLabel()}"
    }

    private fun getTemperatureLabel(): String {
        return when (weatherService.getTemperature()) {
            Temperature.VERY_COLD -> "frozen"
            Temperature.COLD -> "cold"
            Temperature.TEMPERATE -> "mild"
            Temperature.WARM -> "warm"
            Temperature.HOT -> "hot"
            Temperature.VERY_HOT -> "scorching"
        }
    }

    private fun getBiomeLabel(): String {
        return when (request.room.biome) {
            BiomeType.TUNDRA -> "tundra"
            BiomeType.PLAINS -> "plains"
            BiomeType.MOUNTAIN -> "mountainous"
            BiomeType.BADLANDS -> "sulfurous swamp"
            BiomeType.JUNGLE -> "jungle"
            BiomeType.DESERT -> "desert"
            BiomeType.ARBOREAL -> "forest"
            BiomeType.SKY -> "flight"
            BiomeType.UNDERGROUND -> "cave"
            BiomeType.NONE -> "ethereal realm"
        }
    }

    private fun getRoomDescription(): String {
        val room = request.room

        val biomeSlug = when (room.biome) {
            BiomeType.ARBOREAL -> "A forest of evergreen trees."
            BiomeType.DESERT -> "Rolling dunes of sand spread out before you."
            BiomeType.JUNGLE -> "A dense jungle."
            BiomeType.BADLANDS -> "A sulphurous smell permeates rocky and unwelcoming terrain."
            BiomeType.MOUNTAIN -> "Mountainous rocks."
            BiomeType.PLAINS -> "A flat grassland."
            BiomeType.TUNDRA -> "A cold, flat land."
            BiomeType.UNDERGROUND -> "Surrounded by rock, deep underground."
            BiomeType.SKY -> "Flying in the sky!"
            BiomeType.NONE -> "Floating in nether."
        }

        val nearbySlug = room.getAllExits().entries.joinToString {
            "${getDirectionString(it.key)} ${getRoomBrief(it.value)}."
        }

        val weatherSlug = when (weatherService.getWeather()) {
            Weather.BLIZZARD -> "A strong blizzard is bearing down."
            Weather.CLEAR -> "The sky is blue with barely a cloud in sight."
            Weather.BLUSTERY -> "Mighty gusts of wind blast against you."
            Weather.OVERCAST -> "A blanket of clouds gives a cover of gray skies."
            Weather.STORMING -> "Angry storm clouds boom thunder and lightning from the heavens."
        }

        return "$biomeSlug\n$nearbySlug\n$weatherSlug"
    }

    private fun getRoomBrief(room: RoomDAO): String {
        return when (room.biome) {
            BiomeType.ARBOREAL -> "is a forest of evergreen trees."
            BiomeType.DESERT -> "rolling dunes of sand spread out before you."
            BiomeType.JUNGLE -> "is a dense jungle."
            BiomeType.BADLANDS -> "a sulphurous smell permeates rocky and unwelcoming terrain."
            BiomeType.MOUNTAIN -> "is a large mountain of rocks."
            BiomeType.PLAINS -> "is a flat grassland."
            BiomeType.TUNDRA -> "is a cold, flat land."
            BiomeType.UNDERGROUND -> "is surrounded by rock, deep underground."
            BiomeType.SKY -> "is the sky."
            BiomeType.NONE -> "is nether."
        }
    }
}
