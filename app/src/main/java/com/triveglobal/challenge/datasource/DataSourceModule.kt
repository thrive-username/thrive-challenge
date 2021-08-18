package com.triveglobal.challenge.datasource

import com.triveglobal.challenge.datasource.local.LocalBookDataSource
import com.triveglobal.challenge.datasource.local.RoomLocalBookDataSource
import com.triveglobal.challenge.datasource.remote.RemoteBookDataSource
import com.triveglobal.challenge.datasource.remote.RetrofitRemoteBookDataSource
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface DataSourceModule {

    @Binds
    @Singleton
    fun bindRemoteBookDataSource(impl: RetrofitRemoteBookDataSource): RemoteBookDataSource

    @Binds
    @Singleton
    fun bindLocalDataSource(impl: RoomLocalBookDataSource): LocalBookDataSource

}