package com.fakhrirasyids.technicalnestedlist.ui.main

import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes.AddJokesUseCase
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addJokesUseCase: AddJokesUseCase
) {

}