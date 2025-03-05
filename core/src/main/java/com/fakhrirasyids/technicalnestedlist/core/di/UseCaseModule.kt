package com.fakhrirasyids.technicalnestedlist.core.di

import com.fakhrirasyids.technicalnestedlist.core.domain.repo.MainRepository
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes.AddJokesInteractor
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes.AddJokesUseCase
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories.GetCategoriesInteractor
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories.GetCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetCategoriesUseCase(repository: MainRepository): GetCategoriesUseCase =
        GetCategoriesInteractor(repository)

    @Provides
    fun provideAddJokesUseCase(repository: MainRepository): AddJokesUseCase =
        AddJokesInteractor(repository)
}
