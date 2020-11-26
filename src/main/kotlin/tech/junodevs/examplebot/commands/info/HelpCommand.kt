package tech.junodevs.examplebot.commands.info

import tech.junodevs.examplebot.commands.CommandCategories
import tech.junodevs.examplebot.settings.ExampleGuildSettings
import net.dv8tion.jda.api.EmbedBuilder
import tech.junodevs.discord.kriess.command.Command
import tech.junodevs.discord.kriess.command.CommandEvent

class HelpCommand : Command<ExampleGuildSettings>("help", description = "Shows you help for ExampleBot", category = CommandCategories.INFO){

    override fun handle(event: CommandEvent<ExampleGuildSettings>) {
        event.settingsManager.getSettingsFor(event.guild).thenAccept { guildSettings ->
            val commands = event.commandManager.getCommands().groupBy { it.category }
                .toSortedMap(Comparator.comparingInt { it.priority })

            val embed = EmbedBuilder().run {
                setDescription(commands.map { category ->
                    val categoryCommands = category.value.filter { command -> command.showInHelp }.joinToString("\n\n") { command ->
                        "`${guildSettings.realPrefix}${command.usage}` - ${command.description}"
                    }

                    "> __**${category.key.name}**__\n\n $categoryCommands"
                }.joinToString("\n\n"))
                setColor(0xC61C7C)
                build()
            }

            event.channel.sendMessage(embed).queue()
        }
    }

}