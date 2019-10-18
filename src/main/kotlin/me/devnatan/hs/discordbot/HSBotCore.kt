package me.devnatan.hs.discordbot

import com.google.common.reflect.ClassPath
import kotlinx.coroutines.CoroutineScope
import me.devnatan.hs.discordbot.api.command.Command
import me.devnatan.hs.discordbot.manager.CommandManager
import me.devnatan.hs.discordbot.manager.ListenerManager
import me.devnatan.hs.discordbot.util.isClassAssignable
import net.dv8tion.jda.api.JDABuilder

class HSBotCore(val builder: JDABuilder, internal val bot: HSBot) : CoroutineScope by bot, Runnable {

    var commandManager = CommandManager(this)

    override fun run() {
        builder.addEventListeners(ListenerManager(this))
        loadCommands()
    }

    private fun loadCommands() {
        for (classInfo in ClassPath.from(this::class.java.classLoader).getTopLevelClassesRecursive("me.devnatan.hs.discordbot.commands")) {
            val info = classInfo.load()
            if (isClassAssignable(info, Command::class.java)) {
                runCatching {
                    (info.kotlin.objectInstance ?: info.newInstance()) as? Command
                }.getOrThrow()?.let {
                    commandManager.register(it)
                }
            }
        }
    }

}