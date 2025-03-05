package com.fakhrirasyids.technicalnestedlist.core.di

import com.fakhrirasyids.technicalnestedlist.core.BuildConfig
import com.fakhrirasyids.technicalnestedlist.core.data.remote.RemoteDataSource
import com.fakhrirasyids.technicalnestedlist.core.data.remote.services.CategoriesApiServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.APP_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

    @Provides
    @Singleton
    fun provideCategoriesApiService(retrofit: Retrofit): CategoriesApiServices =
        retrofit.create(CategoriesApiServices::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: CategoriesApiServices): RemoteDataSource =
        RemoteDataSource(apiService)
}
