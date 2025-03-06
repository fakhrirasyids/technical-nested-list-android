package com.fakhrirasyids.technicalnestedlist.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes.AddJokesUseCase
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories.GetCategoriesUseCase
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import com.fakhrirasyids.technicalnestedlist.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addJokesUseCase: AddJokesUseCase
) : ViewModel() {
    private val _isLoadingCategories = MutableLiveData<Boolean>()
    val isLoadingCategories: LiveData<Boolean> = _isLoadingCategories

    private val _loadingJokesMap = MutableLiveData<Map<String, Boolean>>()
    val loadingJokesMap: LiveData<Map<String, Boolean>> get() = _loadingJokesMap

    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> get() = _categories

    private val _isErrorCategories = MutableLiveData<Boolean>()
    val isErrorCategories: LiveData<Boolean> = _isErrorCategories

    private val _errorCategories = MutableLiveData<String>()
    val errorCategories: LiveData<String> get() = _errorCategories

    private val _errorJokes = MutableLiveData<Event<String>>()
    val errorJokes: LiveData<Event<String>> get() = _errorJokes

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _isErrorCategories.postValue(false)
                        _isLoadingCategories.postValue(true)
                    }

                    is Resource.Success -> {
                        _isLoadingCategories.postValue(false)
                        _categories.postValue(result.data)
                    }

                    is Resource.Error -> {
                        _isLoadingCategories.postValue(false)
                        _isErrorCategories.postValue(true)
                        _errorCategories.postValue(result.error)
                    }
                }
            }
        }
    }

    fun addJokesToCategory(categoryName: String, shouldFetch: Boolean = false) {
        viewModelScope.launch {
            if (_categories.value?.find { it.categoryName == categoryName }?.jokes?.isNotEmpty() == true && !shouldFetch) {
                return@launch
            }

            _loadingJokesMap.postValue(_loadingJokesMap.value.orEmpty() + (categoryName to true))

            addJokesUseCase(categoryName, shouldFetch).collect { result ->
                when (result) {
                    is Resource.Loading -> _loadingJokesMap.postValue(
                        _loadingJokesMap.value.orEmpty() + (categoryName to true)
                    )

                    is Resource.Success -> {
                        _categories.postValue(
                            _categories.value?.map {
                                if (it.categoryName == categoryName) it.copy(jokes = result.data.toMutableList())
                                else it
                            }
                        )
                        _loadingJokesMap.postValue(_loadingJokesMap.value.orEmpty() + (categoryName to false))
                    }

                    is Resource.Error -> {
                        _categories.postValue(
                            _categories.value?.map {
                                if (it.categoryName == categoryName) it.copy(isExpanded = false)
                                else it
                            }
                        )
                        _loadingJokesMap.postValue(_loadingJokesMap.value.orEmpty() + (categoryName to false))
                    }
                }
            }
        }
    }

    fun goToTop(categoryName: String) {
        _categories.value?.let { currentCategories ->
            val updatedList = currentCategories
                .sortedByDescending { it.categoryName == categoryName }
                .mapIndexed { index, category -> category.copy(index = index) }

            _categories.postValue(updatedList)
        }
    }

    fun toggleExpansion(categoryName: String) {
        _categories.postValue(_categories.value?.map {
            it.copy(isExpanded = it.categoryName == categoryName != it.isExpanded)
        })
    }
}