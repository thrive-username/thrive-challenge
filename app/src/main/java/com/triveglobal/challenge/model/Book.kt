package com.triveglobal.challenge.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class Book(
    val author: String,
    val categories: String,
    val id: Long?,
    val lastCheckedOut: DateTime?,
    val lastCheckedOutBy: String?,
    val publisher: String,
    val title: String
): Parcelable
