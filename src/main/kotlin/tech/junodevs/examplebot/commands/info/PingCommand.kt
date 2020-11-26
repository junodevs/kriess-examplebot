package tech.junodevs.examplebot.commands.info

import tech.junodevs.examplebot.commands.CommandCategories
import tech.junodevs.examplebot.settings.ExampleGuildSettings
import tech.junodevs.discord.kriess.command.Command
import tech.junodevs.discord.kriess.command.CommandEvent

class PingCommand : Command<ExampleGuildSettings>(name = "ping", description = "Show's the bot's ping", category = CommandCategories.INFO) {

    override fun handle(event: CommandEvent<ExampleGuildSettings>) {
        event.reply(":boom: Pong! `${event.jda.gatewayPing}ms`")
    }

}