package kotlinmud.player.service

import com.commit451.mailgun.Contact
import com.commit451.mailgun.SendMessageRequest
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.konform.validation.Invalid
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinmud.affect.model.Affect
import kotlinmud.attributes.type.Attribute
import kotlinmud.event.factory.createClientLoggedInEvent
import kotlinmud.event.service.EventService
import kotlinmud.faction.type.FactionType
import kotlinmud.helper.logger
import kotlinmud.helper.random.generateOTP
import kotlinmud.helper.string.matches
import kotlinmud.io.model.Client
import kotlinmud.io.model.PreAuthRequest
import kotlinmud.io.model.PreAuthResponse
import kotlinmud.io.type.IOStatus
import kotlinmud.item.model.Item
import kotlinmud.mob.builder.PlayerMobBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.factory.createRaceFromString
import kotlinmud.mob.race.type.RaceType
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.CurrencyType
import kotlinmud.mob.type.Gender
import kotlinmud.player.auth.impl.CompleteAuthStep
import kotlinmud.player.auth.impl.EmailAuthStep
import kotlinmud.player.auth.service.AuthStepService
import kotlinmud.player.auth.type.AuthStep
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.player.exception.EmailFormatException
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.room.service.RoomService
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import kotlinmud.player.repository.findPlayerByOTP as findPlayerByOTPQuery

class PlayerService(
    private val emailService: EmailService,
    private val eventService: EventService,
    private val mobService: MobService,
    private val roomService: RoomService,
    private val specializations: List<Specialization>
) {
    private val preAuthClients: MutableMap<Client, AuthStep> = mutableMapOf()
    private val loggedInPlayers: MutableMap<Int, PlayerDAO> = mutableMapOf()
    private val loggedInMobs: MutableMap<PlayerDAO, PlayerMob> = mutableMapOf()
    private lateinit var authStepService: AuthStepService
    private val logger = logger(this)

    fun setAuthStep(client: Client, authStep: AuthStep) {
        preAuthClients[client] = authStep
    }

    fun setAuthStepService(authStepService: AuthStepService) {
        this.authStepService = authStepService
    }

    suspend fun handlePreAuthRequest(request: PreAuthRequest): PreAuthResponse {
        val authStep = preAuthClients[request.client] ?: EmailAuthStep(authStepService)
        val ioStatus = authStep.handlePreAuthRequest(request)
        logger.debug("pre-auth request :: {}, {}, {}", authStep.authorizationStep, request.input, ioStatus)
        val nextAuthStep = if (ioStatus == IOStatus.OK) {
            proceedAuth(request, authStep)
        } else authStep
        return PreAuthResponse(
            request,
            ioStatus,
            if (ioStatus == IOStatus.OK) "ok." else authStep.errorMessage,
            nextAuthStep
        )
    }

    fun findPlayerMobByName(name: String): PlayerMob? {
        return mobService.findPlayerMobByName(name)
    }

    fun findLoggedInPlayerMobByName(name: String): Mob? {
        return loggedInMobs.values.find { name.matches(it.name) }
    }

    fun findPlayerByOTP(otp: String): PlayerDAO? {
        return findPlayerByOTPQuery(otp)
    }

    fun createNewPlayerWithEmailAddress(emailAddress: String): PlayerDAO {
        validateEmailAddressFormat(emailAddress)
        return transaction {
            PlayerDAO.new {
                email = emailAddress
                name = "foo"
            }
        }
    }

    fun sendOTP(player: PlayerDAO) {
        val from = Contact("floodle@danmunro.com", "Floodle")
        val to = mutableListOf(Contact(player.email, "Login OTP"))
        val otp = generateOTP()
        emailService.sendEmail(
            SendMessageRequest.Builder(from)
                .to(to)
                .subject("Your OTP mud login")
                .text("Hi,\n\n Here is your OTP login: \"$otp\"\n\nIt will expire five minutes from now.")
                .build()
        )
        transaction { player.lastOTP = otp }
    }

    fun loginClientAsPlayer(client: Client, player: PlayerDAO) {
        loggedInPlayers[client.id] = player
    }

    fun loginPlayerAsMob(player: PlayerDAO, mob: PlayerMob) {
        loggedInMobs[player] = mob
    }

    fun addPreAuthClient(client: Client) {
        preAuthClients[client] = EmailAuthStep(authStepService)
    }

    fun getAuthStepForClient(client: Client): AuthStep? {
        return preAuthClients[client]
    }

    fun dumpPlayerMobData(mob: PlayerMob) {
        val mapper = jacksonObjectMapper()
        File("./players/${mob.name}.json").writeText(mapper.writeValueAsString(mob))
    }

    fun rehydratePlayerMob(name: String): PlayerMob {
        val data = File("./players/$name.json")
        val mapper = jacksonObjectMapper()
        val node: JsonNode = mapper.readTree(data)
        val factionReader = mapper.readerFor(object : TypeReference<Map<FactionType, Int>>() {})
        val questReader = mapper.readerFor(object : TypeReference<Map<QuestType, QuestStatus>>() {})
        val attributeReader = mapper.readerFor(object : TypeReference<Map<Attribute, Int>>() {})
        val skillReader = mapper.readerFor(object : TypeReference<Map<SkillType, Int>>() {})
        val affectReader = mapper.readerFor(object : TypeReference<List<Affect>>() {})
        val currencyReader = mapper.readerFor(object : TypeReference<Map<CurrencyType, Int>>() {})
        val itemReader = mapper.readerFor(object : TypeReference<List<Item>>() {})
        return PlayerMobBuilder(mobService).also {
            val spec = SpecializationType.valueOf(node.get("specialization").asText("NONE"))
            val factionScores: MutableMap<FactionType, Int> = factionReader.readValue(node.get("factionScores"))
            val quests: MutableMap<QuestType, QuestStatus> = questReader.readValue(node.get("quests"))
            val attributes: MutableMap<Attribute, Int> = attributeReader.readValue(node.get("attributes"))
            val skills: MutableMap<SkillType, Int> = skillReader.readValue(node.get("skills"))
            val affects: MutableList<Affect> = affectReader.readValue(node.get("affects"))
            val currencies: MutableMap<CurrencyType, Int> = currencyReader.readValue(node.get("currencies"))
            val items: MutableList<Item> = itemReader.readValue(node.get("items"))
            val equipped: MutableList<Item> = itemReader.readValue(node.get("equipped"))
            val roomId = node.get("room").intValue()
            it.emailAddress = node.get("emailAddress").textValue()
            it.name = node.get("name").textValue()
            it.brief = node.get("brief").textValue()
            it.description = node.get("description").textValue()
            it.experienceToLevel = node.get("experienceToLevel").intValue()
            it.experience = node.get("experience").intValue()
            it.trains = node.get("trains").intValue()
            it.practices = node.get("practices").intValue()
            it.bounty = node.get("bounty").intValue()
            it.sacPoints = node.get("sacPoints").intValue()
            it.hunger = node.get("hunger").intValue()
            it.thirst = node.get("thirst").intValue()
            it.skillPoints = node.get("skillPoints").intValue()
            it.room = roomService.findOne { room -> room.id == roomId }!!
            it.factionScores = factionScores
            it.quests = quests
            it.hp = node.get("hp").intValue()
            it.mana = node.get("mana").intValue()
            it.mv = node.get("mv").intValue()
            it.level = node.get("level").intValue()
            it.race = createRaceFromString(RaceType.valueOf(node.get("race").textValue()))
            it.specialization = specializations.find { specialization -> specialization.type == spec }
            it.gender = Gender.valueOf(node.get("gender").textValue())
            it.wimpy = node.get("wimpy").intValue()
            it.savingThrows = node.get("savingThrows").intValue()
            it.attributes = attributes
            it.equipped = equipped
            it.maxItems = node.get("maxItems").intValue()
            it.maxWeight = node.get("maxWeight").intValue()
            it.items = items
            it.skills = skills
            it.affects = affects
            it.currencies = currencies
        }.build()
    }

    private suspend fun proceedAuth(request: PreAuthRequest, authStep: AuthStep): AuthStep {
        val nextAuthStep = authStep.getNextAuthStep()
        if (nextAuthStep is CompleteAuthStep) {
            loginMob(request.client, nextAuthStep.playerMob)
        }
        preAuthClients[request.client] = nextAuthStep
        request.client.write(nextAuthStep.promptMessage)
        return nextAuthStep
    }

    private fun validateEmailAddressFormat(emailAddress: String) {
        val validateEmail = Validation<String> {
            pattern(".+@.+..+")
        }
        val result = validateEmail(emailAddress)
        if (result is Invalid) {
            throw EmailFormatException()
        }
    }

    private suspend fun loginMob(client: Client, mob: PlayerMob) {
        val player = loggedInPlayers[client.id]!!
        loginPlayerAsMob(player, mob)
        eventService.publish(createClientLoggedInEvent(client, mob))
    }
}
