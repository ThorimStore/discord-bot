package me.devnatan.hs.discordbot.manager

import kotlinx.coroutines.launch
import me.devnatan.hs.discordbot.HSBotCore
import me.devnatan.hs.discordbot.api.command.Command
import me.devnatan.hs.discordbot.api.command.CommandContext
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CommandManager(private val core: HSBotCore) {

    private val commands: ArrayList<Command> = arrayListOf()

    fun register(command: Command) {
        commands.add(command)
        core.bot.logger.info("Command \"${command.name}\" loaded.")
    }

    fun handle(event: MessageReceivedEvent): Boolean {
        val raw = event.message.contentRaw

        // check prefix first
        if (!check(raw))
            return false

        val input = raw.substring(core.bot.prefix.length until raw.length)
        if (!input.contains(" ")) {
            if (!exists(input))
                return false

            val command = find(input)!!
            if (!command.handle(input, emptyArray()))
                return false

            event.message.delete().queue {
                core.launch {
                    command(CommandContext(event), input, emptyArray())
                }
            }
        } else {
            val split = input.split(" ")
            val command = split[0]
            if (!exists(command))
                return false

            val args = split.drop(1)
            val raw = args.joinToString(" ")
            val argsArr = args.toTypedArray()
            val instance = find(command)!!
            if (!instance.handle(raw, argsArr))
                return false

            event.message.delete().queue {
                core.launch {
                    instance(CommandContext(event), raw, argsArr)
                }
            }
        }
        return true
    }

    fun exists(command: String): Boolean {
        if (commands.any { it.name == command })
            return true

        return commands.filter { it.aliases != null }.any { it.aliases!!().contains(command) }
    }

    fun find(command: String): Command? {
        return commands.find { it.name == command }
            ?: commands.filter { it.aliases != null }.find { it.aliases!!().contains(command) }
    }

    fun check(raw: String): Boolean {
        val prefix = core.bot.prefix

        // prefix may exist, but no commands.
        if (raw.length <= prefix.length)
            return false

        for (i in prefix.indices) {
            // current prefix and message prefix are not the same.
            if (prefix[i] != raw[i])
                return false
        }

        return true
    }

}