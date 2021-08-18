package com.triveglobal.challenge

import com.triveglobal.challenge.model.Book
import java.util.*

object BookMockHelper {

    private val random = Random()

    fun createBook(id: Long? = random.nextLong()) = Book(
        "author${random.nextInt()}",
        "categories${random.nextInt()}",
        id,
        null,
        null,
        "publisher${random.nextInt()}",
        "title${random.nextInt()}"
    )

}