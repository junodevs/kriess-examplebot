package tech.junodevs.examplebot.settings

import tech.junodevs.discord.kriess.impl.managers.GuildSettingsManager

class ExampleGuildSettingsManager : GuildSettingsManager<ExampleGuildSettings>() {
    override fun createAbsentInstance(guildId: Long): ExampleGuildSettings {
        return ExampleGuildSettings(guildId)
    }

    override fun createInstance(guildId: Long, properties: Map<String, Any?>): ExampleGuildSettings {
        return ExampleGuildSettings(guildId, properties["prefix"] as String?)
    }
}