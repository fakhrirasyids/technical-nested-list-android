package com.fakhrirasyids.technicalnestedlist.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fakhrirasyids.technicalnestedlist.databinding.ActivityMainBinding
import com.fakhrirasyids.technicalnestedlist.ui.adapters.CategoryAccordionAdapter
import com.fakhrirasyids.technicalnestedlist.utils.enums.DialogType
import com.fakhrirasyids.technicalnestedlist.utils.helpers.Dialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    private val categoryAccordionAdapter = CategoryAccordionAdapter()

    private val customDialog: Dialog by lazy {
        Dialog(this@MainActivity)
    }

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
            loadingJokesMap.observe(this@MainActivity, categoryAccordionAdapter::setLoadingState)
            categories.observe(this@MainActivity, categoryAccordionAdapter::submitList)
            isErrorCategories.observe(this@MainActivity, ::showError)

            errorCategories.observe(this@MainActivity) { message ->
                binding.tvError.text = message
            }

            errorJokes.observe(this@MainActivity) { event ->
                event.getContentIfNotHandled()?.let { message ->
                    customDialog.showDialog(DialogType.ERROR, message, onDismiss = {
                        event.handleContent()
                    })
                }
            }

            dialogJokes.observe(this@MainActivity) { event ->
                event.getContentIfNotHandled()?.let { message ->
                    customDialog.showDialog(DialogType.INFO, message, onDismiss = {
                        event.handleContent()
                    })
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
                    rvCategories.postDelayed({
                        rvCategories.smoothScrollToPosition(0)
                    }, 200)
                }

                onAddJokeClick = { category ->
                    mainViewModel.addJokesToCategory(category, true)
                }

                onExpansionClick = { category, position ->
                    mainViewModel.addJokesToCategory(category)
                    mainViewModel.toggleExpansion(category)

                    notifyItemChanged(position)
                }

                onJokeClick = { joke ->
                    mainViewModel.setDialogJokes(joke)
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

            swipeRefreshLayout.isActivated = !isLoading
            rvCategories.isVisible = !isLoading
        }
    }

    private fun showError(isError: Boolean) {
        binding.apply {
            if (isError) {
                layoutError.visibility = View.VISIBLE
                rvCategories.visibility = View.GONE
            } else {
                layoutError.visibility = View.GONE
                rvCategories.visibility = View.VISIBLE
            }
        }
    }
}