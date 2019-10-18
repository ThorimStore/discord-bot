package me.devnatan.hs.discordbot.util

import java.net.URL

fun String.isUrl(): Boolean {
    return try {
        URL(this)
        true
    } catch (e: Throwable) {
        false
    }
}