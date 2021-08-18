package com.triveglobal.challenge.model

import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Constants {

    object Date {
        val FORMATTER: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss zzz")
    }

}