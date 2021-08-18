package com.triveglobal.challenge.extensions

import com.triveglobal.challenge.model.Constants
import com.triveglobal.challenge.model.Constants.Date.FORMATTER
import org.joda.time.DateTime

/**
 * Attempts to parse a string with the format
 * yyyy-MM-dd HH:mm:ss zzz into a [DateTime],
 * if THIS string is null or has an invalid format
 * it returns null
 */
fun String?.parseDateTime(): DateTime? {
     return try {
         this?.let { FORMATTER.parseDateTime(it) }
     } catch (e: UnsupportedOperationException){
         null
     } catch (e: IllegalArgumentException) {
         null
     }
}