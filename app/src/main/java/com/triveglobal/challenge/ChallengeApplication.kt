package com.triveglobal.challenge

import com.triveglobal.challenge.di.ApplicationModule
import com.triveglobal.challenge.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class ChallengeApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(ApplicationModule(this))
    }

}