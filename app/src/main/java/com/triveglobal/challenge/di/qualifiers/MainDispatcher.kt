package com.triveglobal.challenge.di.qualifiers


import javax.inject.Qualifier

/**
 * Qualifier for coroutine dispatcher Main thread
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class MainDispatcher
