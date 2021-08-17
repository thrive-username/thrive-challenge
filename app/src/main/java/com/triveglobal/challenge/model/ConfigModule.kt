package com.triveglobal.challenge.model

import com.triveglobal.challenge.BuildConfig
import com.triveglobal.challenge.di.qualifiers.BackendBaseUrl
import dagger.Module
import dagger.Provides

@Module
class ConfigModule {

    @Provides
    @BackendBaseUrl
    fun provideBackendBaseUrl(): String = BuildConfig.BASE_URL

}