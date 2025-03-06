package com.fakhrirasyids.technicalnestedlist.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.addjokes.AddJokesUseCase
import com.fakhrirasyids.technicalnestedlist.core.domain.usecase.getcategories.GetCategoriesUseCase
import com.fakhrirasyids.technicalnestedlist.core.utils.Resource
import com.fakhrirasyids.technicalnestedlist.utils.helpers.Event
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
    val loadingJokesMap: LiveData<Map<String, Boolean>> = _loadingJokesMap

    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> get() = _categories

    private val _isErrorCategories = MutableLiveData<Boolean>()
    val isErrorCategories: LiveData<Boolean> = _isErrorCategories

    private val _errorCategories = MutableLiveData<String>()
    val errorCategories: LiveData<String> get() = _errorCategories

    private val _errorJokes = MutableLiveData<Event<String>>()
    val errorJokes: LiveData<Event<String>> get() = _errorJokes

    private val _dialogJokes = MutableLiveData<Event<String>>()
    val dialogJokes: LiveData<Event<String>> get() = _dialogJokes

    init {
        fetchCategories()
    }

    fun fetchCategories() {
        viewModelScope.launch {
            getCategoriesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _isErrorCategories.value = false
                        _isLoadingCategories.value = true
                        _loadingJokesMap.value = emptyMap()
                    }

                    is Resource.Success -> {
                        _isLoadingCategories.value = false
                        _categories.value = result.data
                        _loadingJokesMap.value = (
                                result.data.associate { category -> category.categoryName to false }
                                )
                    }

                    is Resource.Error -> {
                        _isLoadingCategories.value = false
                        _isErrorCategories.value = true
                        _errorCategories.value = result.error
                        _loadingJokesMap.value = emptyMap()
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

            _loadingJokesMap.value = _loadingJokesMap.value.orEmpty().toMutableMap().apply {
                this[categoryName] = true
            }

            addJokesUseCase(categoryName, shouldFetch).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _loadingJokesMap.value =
                            _loadingJokesMap.value.orEmpty().toMutableMap().apply {
                                this[categoryName] = true
                            }
                    }

                    is Resource.Success -> {
                        val updatedCategories = _categories.value?.map { category ->
                            if (category.categoryName == categoryName) {
                                category.copy(jokes = result.data)
                            } else {
                                category
                            }
                        } ?: emptyList()

                        _categories.value = updatedCategories

                        _loadingJokesMap.value =
                            _loadingJokesMap.value.orEmpty().toMutableMap().apply {
                                this[categoryName] = false
                            }
                    }

                    is Resource.Error -> {
                        val updatedCategories = _categories.value?.map {
                            if (it.categoryName == categoryName) it.copy(isExpanded = false)
                            else it
                        } ?: emptyList()

                        _categories.value = updatedCategories

                        _loadingJokesMap.value =
                            _loadingJokesMap.value.orEmpty().toMutableMap().apply {
                                this[categoryName] = false
                            }

                        _errorJokes.value = Event(result.error)
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

            _categories.value = updatedList
        }
    }

    fun toggleExpansion(categoryName: String) {
        val updatedValue = _categories.value?.map {
            it.copy(isExpanded = it.categoryName == categoryName != it.isExpanded)
        } ?: emptyList()

        _categories.value = updatedValue
    }

    fun setDialogJokes(joke: String) {
        _dialogJokes.value = Event(joke)
    }
}