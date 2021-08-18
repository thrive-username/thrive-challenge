package com.triveglobal.challenge.ui

import com.triveglobal.challenge.ui.feed.BookFeedInjector
import dagger.Module

@Module(includes = [BookFeedInjector::class])
class UIModule {
}