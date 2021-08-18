package com.triveglobal.challenge.extensions

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.Assert
import org.junit.Test

class StringExtensions {

    @Test
    fun parseDateTime_null_null() {
        val serializedDateTime: String? = null
        val result = serializedDateTime.parseDateTime()
        Assert.assertNull(result)
    }

    @Test
    fun parseDateTime_invalidDateFormat_null() {
        val serializedDateTime = "WRONG_FORMAT"
        val result = serializedDateTime.parseDateTime()
        Assert.assertNull(result)
    }

    @Test
    fun serializeToString_serializedDate_dateTime() {
        val serializedDateTime = "2021-12-05 23:45:37 UTC"
        val result = serializedDateTime.parseDateTime()
        Assert.assertEquals(DateTime(2021, 12, 5, 23, 45, 37, DateTimeZone.UTC), result)
    }

}