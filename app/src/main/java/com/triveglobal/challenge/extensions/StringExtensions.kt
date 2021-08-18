package com.triveglobal.challenge.extensions

import org.joda.time.DateTime

/**
 * Attempts to parse a string with the format
 * yyyy-MM-dd HH:mm:ss zzz into a [DateTime],
 * if THIS string is null or has an invalid format
 * it returns null
 */
fun String?.deserializeToDateTime(): DateTime? {
    TODO()
}