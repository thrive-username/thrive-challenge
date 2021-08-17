package com.triveglobal.challenge.network

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NetworkModule {

    @Binds
    @Singleton
    fun bindNetworkAPI(impl: RetrofitNetworkAPI): NetworkAPI

}