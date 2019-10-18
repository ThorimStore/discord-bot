package me.devnatan.hs.discordbot.util

tailrec fun isClassAssignable(clazz: Class<*>, base: Class<*>): Boolean {
    if (base.isAssignableFrom(clazz))
        return true

    val superClass = clazz.superclass ?: return false
    return isClassAssignable(superClass, base)
}