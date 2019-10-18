package me.devnatan.hs.discordbot.api.command

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

open class CommandContext protected constructor(
    val messageId: Long,
    val message: Message,
    val author: User,
    val member: Member?,
    val channel: MessageChannel
) {

    constructor(event: MessageReceivedEvent) : this(
        event.messageIdLong,
        event.message,
        event.author,
        event.member,
        event.channel
    )

}