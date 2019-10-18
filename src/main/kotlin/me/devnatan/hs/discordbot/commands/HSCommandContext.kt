package me.devnatan.hs.discordbot.commands

import me.devnatan.hs.discordbot.HSBotCore
import me.devnatan.hs.discordbot.api.command.CommandContext
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

open class HSCommandContext(private val core: HSBotCore, event: MessageReceivedEvent) : CommandContext(event)