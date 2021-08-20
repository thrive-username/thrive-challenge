package com.triveglobal.challenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class LiveDataTestObserver<T> private constructor() : Observer<T> {

    private val _emittedValues = mutableListOf<T?>()
    val emittedValues: List<T?> = _emittedValues

    companion object {
        fun <T> LiveData<T>.createTestObserver(): LiveDataTestObserver<T> {
            val subscriber = LiveDataTestObserver<T>()
            observeForever(subscriber)
            return subscriber
        }
    }

    override fun onChanged(t: T?) {
        _emittedValues.add(t)
    }
}