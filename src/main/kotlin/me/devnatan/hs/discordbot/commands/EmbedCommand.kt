package me.devnatan.hs.discordbot.commands

import com.google.common.net.UrlEscapers
import me.devnatan.hs.discordbot.api.command.Command
import me.devnatan.hs.discordbot.api.command.CommandContext
import me.devnatan.hs.discordbot.api.util.StringArray
import me.devnatan.hs.discordbot.util.Colors
import me.devnatan.hs.discordbot.util.isUrl
import net.dv8tion.jda.api.EmbedBuilder
import java.awt.Color
import java.time.Instant

class EmbedCommand : Command("embed") {

    override fun handle(raw: String, args: StringArray): Boolean {
        return args.isNotEmpty()
    }

    override suspend fun invoke(context: CommandContext, raw: String, args: StringArray) {
        if (args.isEmpty())
            return

        val message = raw.split("\n")
        val author = context.author
        if (message.size == 1) {
            context.channel.sendMessage(
                EmbedBuilder()
                    .appendDescription(raw)
                    .setTimestamp(Instant.now())
                    .setFooter("Publicado por " + author.name, author.avatarUrl)
                    .setColor(Colors.EMBED_TRANSPARENT)
                    .build()
            ).queue()
        } else {
            val fullTitle = message[0]
            var title = fullTitle
            var url = if (fullTitle.contains(" ")) {
                val split = fullTitle.split(" ")
                val last = split.last()
                if (last.isUrl()) {
                    title = split.dropLast(1).joinToString(" ")
                    last
                } else
                    null
            } else
                null

            if (url != null && !url.isUrl())
                url = null

            val description = message.drop(1).joinToString("\n")
            context.channel.sendMessage(
                EmbedBuilder()
                    .appendDescription(description)
                    .setTitle(title, url)
                    .setTimestamp(Instant.now())
                    .setFooter("Publicado por " + author.name, author.avatarUrl)
                    .setColor(Colors.EMBED_TRANSPARENT)
                    .build()
            ).queue()
        }
    }

}