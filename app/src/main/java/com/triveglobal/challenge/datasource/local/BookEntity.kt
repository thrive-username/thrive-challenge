package com.triveglobal.challenge.datasource.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookEntity(@ColumnInfo val author: String,
                      @ColumnInfo val categories: String,
                      @PrimaryKey val id: Long,
                      @ColumnInfo val lastCheckedOut: String?,
                      @ColumnInfo val lastCheckedOutBy: String?,
                      @ColumnInfo val publisher: String,
                      @ColumnInfo val title: String)