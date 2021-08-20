package com.triveglobal.challenge.extensions

import androidx.lifecycle.MutableLiveData

/**
 * Using the last value (if any),
 * transforms it using the [transformFunction]
 * and emits  it.
 * This methods is meant to be used  when [T] has a copy function, for example, data classes
 * @sample mutableLiveData.copyFromLast{ copy(someProperty = newValue) }
 */
fun <T> MutableLiveData<T>.copyAndTransformLastValue(transformFunction: T.()->T) {
    value?.let { value = transformFunction(it) }
}