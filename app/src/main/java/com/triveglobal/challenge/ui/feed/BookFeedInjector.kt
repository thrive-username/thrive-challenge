package com.triveglobal.challenge.ui.feed

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface BookFeedInjector {

    @ContributesAndroidInjector
    fun injectorForBookFeedFragment(): BookFeedFragment

}