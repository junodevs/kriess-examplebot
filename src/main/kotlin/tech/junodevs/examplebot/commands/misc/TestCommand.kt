package tech.junodevs.examplebot.commands.misc

import tech.junodevs.examplebot.commands.CommandCategories
import tech.junodevs.examplebot.settings.ExampleGuildSettings
import tech.junodevs.discord.kriess.command.Command
import tech.junodevs.discord.kriess.command.CommandEvent

class TestCommand : Command<ExampleGuildSettings>(name = "test", description = "A test command", category = CommandCategories.MISC) {

    override fun handle(event: CommandEvent<ExampleGuildSettings>) {
        event.reply("Hello, World!")
    }

}