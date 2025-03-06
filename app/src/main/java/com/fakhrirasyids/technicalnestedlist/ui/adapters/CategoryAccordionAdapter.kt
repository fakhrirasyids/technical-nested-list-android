package com.fakhrirasyids.technicalnestedlist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhrirasyids.technicalnestedlist.R
import com.fakhrirasyids.technicalnestedlist.core.domain.model.Categories
import com.fakhrirasyids.technicalnestedlist.databinding.ItemCategoryRowBinding

class CategoryAccordionAdapter(
    var onExpansionClick: ((String) -> Unit)? = null,
    var onAddJokeClick: ((String) -> Unit)? = null,
    var onGoToTopClick: ((String) -> Unit)? = null,
    var onJokeClick: ((String) -> Unit)? = null,
) : ListAdapter<Categories, CategoryAccordionAdapter.CategoryViewHolder>(DIFF_CALLBACK) {

    private var loadingJokesMap = mutableMapOf<String, Boolean>()

    fun setLoadingState(newLoadingMap: Map<String, Boolean>) {
        val diffResult = loadingJokesMap.entries
            .filter { (key, value) -> newLoadingMap[key] != value }
            .mapNotNull { (category, _) ->
                currentList.indexOfFirst { it.categoryName == category }.takeIf { it != -1 }
            }

        loadingJokesMap = newLoadingMap.toMutableMap()

        diffResult.forEach {
            notifyItemChanged(
                it,
                newLoadingMap[currentList[it].categoryName] ?: false
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        val isLoading = loadingJokesMap[category.categoryName] ?: false
        holder.bind(category, isLoading)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val jokeAdapter = JokeAdapter { joke -> onJokeClick?.invoke(joke) }

        fun bind(category: Categories, isLoadingJokes: Boolean) {
            binding.apply {
                tvCategoryName.text = category.categoryName

                btnGoToTop.apply {
                    text = if (adapterPosition == 0) ContextCompat.getString(
                        root.context,
                        R.string.text_top
                    ) else ContextCompat.getString(root.context, R.string.text_go_to_top)
                    isEnabled = adapterPosition != 0
                }

                recyclerViewJokes.apply {
                    adapter = jokeAdapter
                    layoutManager = LinearLayoutManager(binding.root.context)
                }

                jokeAdapter.submitList(category.jokes)

                updateLoadingState(isLoadingJokes)
                toggleExpansion(category.isExpanded)

                root.setOnClickListener {
                    onExpansionClick?.invoke(category.categoryName)
                    notifyItemChanged(adapterPosition)
                }

                btnGoToTop.setOnClickListener {
                    onGoToTopClick?.invoke(category.categoryName)
                }

                btnAddJoke.apply {
                    isEnabled = !isLoadingJokes
                    setOnClickListener { onAddJokeClick?.invoke(category.categoryName) }
                }
            }
        }

        private fun updateLoadingState(isLoadingJokes: Boolean) {
            binding.shimmerJokes.apply {
                isVisible = isLoadingJokes
                if (isLoadingJokes) startShimmer() else stopShimmer()
            }
        }

        private fun toggleExpansion(expand: Boolean) {
            binding.apply {
                ivDropdown.rotation = if (expand) 180f else 0f
                recyclerViewJokes.isVisible = expand
                btnAddJoke.isVisible = expand
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Categories>() {
            override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean =
                oldItem.categoryName == newItem.categoryName

            override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean =
                oldItem == newItem && oldItem.isExpanded == newItem.isExpanded
        }
    }
}
