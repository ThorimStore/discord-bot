package me.devnatan.hs.discordbot.manager

import me.devnatan.hs.discordbot.HSBotCore
import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ListenerManager(private val core: HSBotCore) : ListenerAdapter() {

    override fun onMessageReceived(e: MessageReceivedEvent) {
        if (!e.isFromGuild || e.author.isBot)
            return

        core.commandManager.handle(e)
    }

}