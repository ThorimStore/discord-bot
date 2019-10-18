package me.devnatan.hs.discordbot.api.command

import me.devnatan.hs.discordbot.api.util.LazyInvoker
import me.devnatan.hs.discordbot.api.util.StringArray

abstract class Command(
    val name: String,
    val aliases: LazyInvoker<StringArray> = null
) {

    abstract fun handle(raw: String, args: StringArray): Boolean

    abstract suspend operator fun invoke(context: CommandContext, raw: String, args: StringArray)

}