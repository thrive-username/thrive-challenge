package com.triveglobal.challenge.extensions

import com.triveglobal.challenge.model.Constants.Date.FORMATTER
import org.joda.time.DateTime

/**
 * Serialize THIS [DateTime] into a String
 * using the following format yyyy-MM-dd HH:mm:ss zzz
 */
fun DateTime?.serializeToString(): String? {
    return this?.let { FORMATTER.print(it) }
}