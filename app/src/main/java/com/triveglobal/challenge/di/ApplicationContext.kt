package com.triveglobal.challenge.di


import javax.inject.Qualifier

/**
 * Qualifier to distinguish application context
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
@Qualifier
annotation class ApplicationContext
