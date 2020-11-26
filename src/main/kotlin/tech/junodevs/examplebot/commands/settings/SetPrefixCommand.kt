package tech.junodevs.examplebot.commands.settings

import tech.junodevs.examplebot.commands.CommandCategories
import tech.junodevs.examplebot.settings.ExampleGuildSettings
import net.dv8tion.jda.api.Permission
import tech.junodevs.discord.kriess.command.Command
import tech.junodevs.discord.kriess.command.CommandEvent

class SetPrefixCommand : Command<ExampleGuildSettings>(name = "setprefix", description = "Set this guild's prefix", category = CommandCategories.SETTINGS) {

    override fun handle(event: CommandEvent<ExampleGuildSettings>) {
        if (!event.member.hasPermission(Permission.MANAGE_SERVER)) {
            return event.replyError("Only admins (those with **Manage Server**) can edit this!")
        }

        event.settingsManager.editSettings(event.guild) {
            if (event.args.isEmpty() || event.args[0].toLowerCase() == "none") {
                prefix = null;
                event.channel.sendMessage(":white_check_mark: Removed custom prefix, you are now using the default: `$realPrefix`").queue()
            } else {
                prefix = event.args[0]
                event.channel.sendMessage(":white_check_mark: Updated prefix to `$prefix`!").queue()
            }
        }
    }

}