package com.triveglobal.challenge.datasource.remote

import com.google.gson.*
import com.triveglobal.challenge.extensions.parseDateTime
import com.triveglobal.challenge.extensions.serializeToString
import org.joda.time.DateTime
import java.lang.reflect.Type

class DateTimeTypeAdapter : JsonSerializer<DateTime>, JsonDeserializer<DateTime>{
    override fun serialize(
        src: DateTime?,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.serializeToString())
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): DateTime? {
        return json?.asString.parseDateTime()
    }


}