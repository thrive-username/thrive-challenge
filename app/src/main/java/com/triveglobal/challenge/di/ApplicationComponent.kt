package com.triveglobal.challenge.di

import com.triveglobal.challenge.ChallengeApplication
import com.triveglobal.challenge.network.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(modules = [AndroidSupportInjectionModule::class,
    ApplicationModule::class,
    NetworkModule::class])
interface ApplicationComponent : AndroidInjector<ChallengeApplication>{
    @Component.Factory
    interface Factory {
        fun create(applicationModule: ApplicationModule): ApplicationComponent
    }
}