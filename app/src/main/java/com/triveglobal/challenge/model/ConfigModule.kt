package com.triveglobal.challenge.model

import com.triveglobal.challenge.BuildConfig
import com.triveglobal.challenge.di.qualifiers.BackendBaseUrl
import dagger.Module
import dagger.Provides
import org.joda.time.DateTime

@Module
class ConfigModule {

    @Provides
    @BackendBaseUrl
    fun provideBackendBaseUrl(): String = BuildConfig.BASE_URL

    @Provides
    fun provideDateTimeProvider(): DateTimeProvider = object : DateTimeProvider {
        override val currentDateTime: DateTime
            get() = DateTime.now()
    }

}