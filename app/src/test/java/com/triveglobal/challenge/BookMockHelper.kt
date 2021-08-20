package com.triveglobal.challenge

import com.triveglobal.challenge.model.Book
import org.joda.time.DateTime
import java.util.*

object BookMockHelper {

    private val random = Random()

    fun createBook(id: Long? = random.nextLong(), lastCheckedOut: DateTime? = null, lastCheckedOutBy: String? = null) = Book(
        "author${random.nextInt()}",
        "categories${random.nextInt()}",
        id,
        lastCheckedOut,
        lastCheckedOutBy,
        "publisher${random.nextInt()}",
        "title${random.nextInt()}"
    )

}