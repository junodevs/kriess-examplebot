package tech.junodevs.examplebot.settings

import tech.junodevs.examplebot.ExampleBot
import tech.junodevs.discord.kriess.providers.GuildSettingsProvider

class ExampleGuildSettings(val guildid: Long, var prefix: String? = null) : GuildSettingsProvider {

    override val realPrefix get() = prefix ?: ExampleBot.prefix

    override fun toMap(): Map<String, Any?> {
        return mapOf(
            "prefix" to prefix
        )
    }

}