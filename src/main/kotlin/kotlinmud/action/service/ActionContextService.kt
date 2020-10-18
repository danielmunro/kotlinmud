package kotlinmud.action.service

import kotlinmud.action.model.ActionContextList
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.type.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createSocialEvent
import kotlinmud.event.service.EventService
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.Response
import kotlinmud.io.service.RequestService
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
import kotlinmud.mob.repository.findMobsForRoom
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.service.PlayerService
import kotlinmud.player.social.Social
import kotlinmud.room.dao.ResourceDAO
import kotlinmud.room.dao.RoomDAO
import kotlinmud.room.helper.getRoomDescription
import kotlinmud.room.helper.getRoomName
import kotlinmud.room.repository.findStartRoom
import kotlinmud.room.type.Direction
import kotlinmud.weather.service.WeatherService
import kotlinmud.weather.type.Weather
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
    private val request: RequestService
) {
    private val craftingService = CraftingService(itemService)
    val recipes = createRecipeList()

    fun craft(recipe: Recipe): List<ItemDAO> {
        return craftingService.craft(recipe, request.getMob())
    }

    fun harvest(resource: ResourceDAO): List<ItemDAO> {
        return craftingService.harvest(resource, request.getMob())
    }

    fun getWeather(): Weather {
        return weatherService.getWeather()
    }

    fun getMob(): MobDAO {
        return request.getMob()
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

    fun transferGold(src: MobDAO, dst: MobDAO, amount: Int) {
        mobService.transferGold(src, dst, amount)
    }

    fun getRoom(): RoomDAO {
        return request.getRoom()
    }

    fun getExits(): Map<Direction, RoomDAO> {
        return request.getRoom().getAllExits()
    }

    fun getRecall(): RoomDAO {
        return transaction {
            val mob = request.getMob()
            if (mob.isNpc) findStartRoom()!! else mob.mobCard!!.respawnRoom
        }
    }

    fun <T> get(syntax: Syntax): T {
        return actionContextList.getResultBySyntax(syntax)
    }

    fun getMobsInRoom(): List<MobDAO> {
        return findMobsForRoom(getRoom())
    }

    suspend fun moveMob(room: RoomDAO, direction: Direction) {
        mobService.moveMob(getMob(), room, direction)
    }

    fun createSpellInvokeResponse(target: Noun, affect: Affect, delay: Int = 1): Response {
        return createOkResponse(
            affect.messageFromInstantiation(getMob(), target),
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
        val target: MobDAO = get(Syntax.MOB_IN_ROOM)
        val fight = mobService.addFight(getMob(), target)
        eventService.publish(fight.createFightStartedEvent())
    }

    suspend fun flee() {
        mobService.flee(getMob())
    }

    suspend fun publishSocial(social: Social) {
        eventService.publish(createSocialEvent(social))
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

    fun getDynamicRoomDescription(): String {
        val room = request.getRoom()
        return "${getRoomName(weatherService.getTemperature(), room.biome)}\n${getRoomDescription(room, weatherService.getWeather())}"
    }

    fun train(attribute: Attribute) {
        mobService.train(getMobCard(), attribute)
    }

    fun practice(skillType: SkillType) {
        mobService.practice(getMob(), getMob().getSkill(skillType)!!)
    }

    fun setDisposition(disposition: Disposition) {
        transaction { getMob().disposition = disposition }
    }
}
