package com.triveglobal.challenge.di

import com.triveglobal.challenge.ChallengeApplication
import com.triveglobal.challenge.datasource.DataSourceModule
import com.triveglobal.challenge.model.ConfigModule
import com.triveglobal.challenge.ui.UIModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    ConfigModule::class,
    DataSourceModule::class,
    UIModule::class])
interface ApplicationComponent : AndroidInjector<ChallengeApplication>{
    @Component.Factory
    interface Factory {
        fun create(applicationModule: ApplicationModule): ApplicationComponent
    }
}