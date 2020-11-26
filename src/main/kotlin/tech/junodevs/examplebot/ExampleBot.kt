package tech.junodevs.examplebot

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import tech.junodevs.examplebot.settings.ExampleGuildSettings
import tech.junodevs.examplebot.settings.ExampleGuildSettingsManager
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.ChunkingFilter
import net.dv8tion.jda.api.utils.MemberCachePolicy
import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.Yaml
import tech.junodevs.discord.kriess.command.Command
import tech.junodevs.discord.kriess.impl.managers.CommandManager
import tech.junodevs.examplebot.commands.info.*
import tech.junodevs.examplebot.commands.misc.*
import tech.junodevs.examplebot.commands.settings.*
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.system.exitProcess

val logger = LoggerFactory.getLogger(ExampleBot.javaClass)


fun main() {
    logger.info("Initializing ExampleBot")
    ExampleBot.load()

    ExampleBot.JDA = JDABuilder
        .createDefault(ExampleBot.token)
        .enableIntents(GatewayIntent.GUILD_MEMBERS)
        .addEventListeners(ExampleBot.commandManager, ExampleBot)
        .setChunkingFilter(ChunkingFilter.ALL)
        .setMemberCachePolicy(MemberCachePolicy.ALL)
        .build()


    println(ExampleBot.JDA.getInviteUrl(
        Permission.MESSAGE_WRITE,
        Permission.MESSAGE_MANAGE,
        Permission.MESSAGE_EMBED_LINKS,
        Permission.MESSAGE_ATTACH_FILES,
        Permission.MESSAGE_ADD_REACTION
    ))

    if (ExampleBot.updateStatus) {
        Timer().scheduleAtFixedRate(0L, 30000L) { ExampleBot.updatePresence() }
    }
}

object ExampleBot : EventListener {
    lateinit var JDA: JDA

    var guildSettingsManager = ExampleGuildSettingsManager()
    lateinit var commandManager: CommandManager<ExampleGuildSettings>

    private val yaml = Yaml()
    lateinit var prefix: String
    lateinit var token: String
    var updateStatus: Boolean = true

    val commands: List<Command<ExampleGuildSettings>> = listOf(
        // Settings
        SetPrefixCommand(),

        // Info
        HelpCommand(),
        PingCommand(),

        // Misc
        TestCommand()
    )

    fun load() {
        val configFile = File("config.yml")
        if (!configFile.exists()) {
            Files.copy(javaClass.getResourceAsStream("/config.yml"), configFile.toPath())

            logger.error("No config file found! Default one copied to current directory.")
            exitProcess(1)
        }

        val output = yaml.load(FileInputStream(configFile)) as Map<String, Any>
        prefix = output["prefix"].toString()
        token = output["token"].toString()
        updateStatus = output["update-status"]?.toString()?.toBoolean() ?: true

        val logLevel = output["log-level"]?.toString() ?: "INFO"

        val level = Level.toLevel(logLevel, Level.INFO)
        (LoggerFactory.getLogger("ROOT") as Logger).level = level

        guildSettingsManager.start()
        commandManager = CommandManager(guildSettingsManager, prefix)

        commands.forEach { command ->
            commandManager.addCommand(command)
        }
    }

    fun updatePresence() {
        JDA.presence.setPresence(
            OnlineStatus.DO_NOT_DISTURB, Activity.watching(
            "over ${JDA.guilds.size} guilds | ${prefix}help"
        ))
    }

    override fun onEvent(event: GenericEvent) {
        when(event) {
            is ReadyEvent -> updatePresence()
        }
    }

}