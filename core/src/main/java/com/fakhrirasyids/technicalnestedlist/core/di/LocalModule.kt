package com.fakhrirasyids.technicalnestedlist.core.di

import android.content.Context
import androidx.room.Room
import com.fakhrirasyids.technicalnestedlist.core.BuildConfig
import com.fakhrirasyids.technicalnestedlist.core.data.local.LocalDataSource
import com.fakhrirasyids.technicalnestedlist.core.data.local.dao.JokeDao
import com.fakhrirasyids.technicalnestedlist.core.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideJokeDao(database: AppDatabase): JokeDao =
        database.jokeDao()

    @Provides
    @Singleton
    fun provideLocalDataSource(jokeDao: JokeDao): LocalDataSource =
        LocalDataSource(jokeDao)
}
