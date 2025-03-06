package com.fakhrirasyids.technicalnestedlist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fakhrirasyids.technicalnestedlist.databinding.ItemJokesRowBinding

class JokeAdapter(
    private val onJokeClick: ((String) -> Unit)? = null
) : ListAdapter<String, JokeAdapter.JokeViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokeViewHolder {
        val binding =
            ItemJokesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JokeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JokeViewHolder, position: Int) {
        val joke = getItem(position)
        holder.bind(joke)
    }

    inner class JokeViewHolder(private val binding: ItemJokesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(joke: String) {
            binding.apply {
                tvJokeText.text = joke
                layoutJokes.setOnClickListener {
                    onJokeClick?.invoke(joke)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}
