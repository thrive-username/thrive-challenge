package com.triveglobal.challenge.ui

import com.triveglobal.challenge.ui.detail.BookDetailsFragment
import com.triveglobal.challenge.ui.feed.BookFeedFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface UIModule {

    @ContributesAndroidInjector
    fun injectorBookFeedFragment(): BookFeedFragment


    @ContributesAndroidInjector
    fun injectBookDetailsFragment(): BookDetailsFragment

}