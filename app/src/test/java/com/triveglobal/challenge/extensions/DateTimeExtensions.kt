package com.triveglobal.challenge.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Assert
import org.junit.Test

class DateTimeExtensions {

    @Test
    fun serializeToString_null_null() {
        val dateTime: DateTime? = null
        val result = dateTime.serializeToString()
        Assert.assertNull(result)
    }

    @Test
    fun serializeToString_date_serializedDate() {
        val dateTime = DateTime(2021, 12, 5, 23, 45, 37, DateTimeZone.UTC)
        val result = dateTime.serializeToString()
        Assert.assertEquals("2021-12-05 23:45:37 UTC", result)
    }

}