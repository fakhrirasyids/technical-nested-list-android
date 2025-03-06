package com.fakhrirasyids.technicalnestedlist.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrirasyids.technicalnestedlist.databinding.ActivityMainBinding
import com.fakhrirasyids.technicalnestedlist.ui.adapters.CategoryAccordionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private val categoryAccordionAdapter = CategoryAccordionAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        setupRecyclerView()
        setListeners()
    }


    private fun observeViewModel() {
        mainViewModel.apply {
            isLoadingCategories.observe(this@MainActivity, ::showCategoryLoading)
            categories.observe(this@MainActivity, categoryAccordionAdapter::submitList)
            loadingJokesMap.observe(this@MainActivity, categoryAccordionAdapter::setLoadingState)
            isErrorCategories.observe(this@MainActivity, ::showError)

            errorCategories.observe(this@MainActivity) { message ->
                message.getContentIfNotHandled()?.let {
                    binding.tvError.text = it
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCategories.apply {
            adapter = categoryAccordionAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setListeners() {
        binding.apply {
            categoryAccordionAdapter.apply {
                onGoToTopClick = { category ->
                    mainViewModel.goToTop(category)
                }

                onAddJokeClick = { category ->
                    mainViewModel.addJokesToCategory(category, true)
                }

                onExpansionClick = { category ->
                    mainViewModel.addJokesToCategory(category)
                    mainViewModel.toggleExpansion(category)
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                mainViewModel.fetchCategories()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun showCategoryLoading(isLoading: Boolean) {
        binding.apply {
            shimmerCategories.apply {
                if (isLoading) {
                    startShimmer()
                } else {
                    stopShimmer()
                }

                isVisible = isLoading
            }

            layoutError.isVisible = !isLoading
            swipeRefreshLayout.isActivated = !isLoading
            rvCategories.isVisible = !isLoading
        }
    }

    private fun showError(isError: Boolean) {
        binding.apply {
            layoutError.isVisible = isError
            shimmerCategories.isVisible = !isError
            rvCategories.isVisible = !isError
        }
    }
}