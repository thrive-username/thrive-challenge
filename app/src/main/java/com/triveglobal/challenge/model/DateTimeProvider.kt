package com.triveglobal.challenge.model

import org.joda.time.DateTime

interface DateTimeProvider {

    val currentDateTime: DateTime

}