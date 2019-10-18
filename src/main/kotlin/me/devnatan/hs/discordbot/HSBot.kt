package me.devnatan.hs.discordbot

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.utils.cache.CacheFlag
import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.apache.commons.cli.ParseException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

class HSBot : CoroutineScope by CoroutineScope(EmptyCoroutineContext + CoroutineName("HSBot")) {

    companion object {

        @JvmStatic
        fun main(args: Array<out String>) {
            HSBot().init(args)
        }

    }

    lateinit var prefix: String
    lateinit var logger: Logger
    lateinit var jda: JDA

    internal fun init(args: Array<out String>) {
        logger = LogManager.getLogger(HSBot::class.java)
        val options = createOptions()
        val parser = DefaultParser()

        val line = try {
            parser.parse(options, args)
        } catch (e: ParseException) {
            logger.error(e.message)
            exitProcess(1)
        }

        prefix = line.getOptionValue("prefix", "!")
        logger.info("Prefix is: \"$prefix\"")
        val builder = JDABuilder(line.getOptionValue("token"))
            .setBulkDeleteSplittingEnabled(false)
            .setDisabledCacheFlags(EnumSet.of(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE))
            .setActivity(Activity.watching("HappyShop ($prefix)"))

        val took = measureTimeMillis {
            HSBotCore(builder, this).run()
            jda = builder.build()
            jda.callbackPool
            jda.awaitReady()
        }

        logger.info("Ready! Took ${System.currentTimeMillis() - took}ms.")
    }

    private fun createOptions(): Options {
        val options = Options()
        options.addOption(Option("t", "token", true, "Bot token").apply { isRequired = true })
        options.addOption(Option("p", "prefix", true, "Bot prefix"))

        return options
    }

}