package com.triveglobal.challenge

import com.triveglobal.challenge.model.Book
import java.util.*

object BookMockHelper {

    private val random = Random()

    fun createBook() = Book(
        "author${random.nextInt()}",
        "categories${random.nextInt()}",
        random.nextLong(),
        null,
        null,
        "publisher${random.nextInt()}",
        "title${random.nextInt()}"
    )

}