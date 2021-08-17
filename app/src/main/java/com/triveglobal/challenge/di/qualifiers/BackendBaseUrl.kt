package com.triveglobal.challenge.di.qualifiers


import javax.inject.Qualifier

/**
 * Qualifier to distinguish the backend base url
 */
@Target(AnnotationTarget.TYPE, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class BackendBaseUrl
