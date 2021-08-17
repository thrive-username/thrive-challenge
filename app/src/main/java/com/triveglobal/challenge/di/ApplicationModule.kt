package com.triveglobal.challenge.di

import android.app.Application
import android.content.Context
import com.triveglobal.challenge.di.qualifiers.ApplicationContext
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Application){

    @Provides
    @ApplicationContext
    fun providesApplicationContext(): Context = application


}