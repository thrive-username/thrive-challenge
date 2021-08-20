package com.triveglobal.challenge.model

import com.triveglobal.challenge.BuildConfig
import com.triveglobal.challenge.di.qualifiers.BackendBaseUrl
import com.triveglobal.challenge.di.qualifiers.IODispatcher
import com.triveglobal.challenge.di.qualifiers.MainDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    @IODispatcher
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO


    @MainDispatcher
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}