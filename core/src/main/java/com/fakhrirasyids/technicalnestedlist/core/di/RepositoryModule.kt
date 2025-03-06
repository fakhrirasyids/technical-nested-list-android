package com.fakhrirasyids.technicalnestedlist.core.di

import com.fakhrirasyids.technicalnestedlist.core.data.local.LocalDataSource
import com.fakhrirasyids.technicalnestedlist.core.data.remote.RemoteDataSource
import com.fakhrirasyids.technicalnestedlist.core.data.repository.MainRepositoryImpl
import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): MainRepository =
        MainRepositoryImpl(remoteDataSource, localDataSource)
}
