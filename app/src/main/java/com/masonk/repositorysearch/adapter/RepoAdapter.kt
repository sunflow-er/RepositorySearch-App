package com.masonk.repositorysearch.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.masonk.repositorysearch.adapter.UserAdapter.Companion.diffUtil
import com.masonk.repositorysearch.databinding.ItemRepoBinding
import com.masonk.repositorysearch.model.Repo

class RepoAdapter(private val onClick: (Repo) -> Unit) : ListAdapter<Repo, RepoAdapter.RepoHolder>(diffUtil) {

    inner class RepoHolder(private val binding: ItemRepoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(repo: Repo) {
            binding.repoNameTextView.text = repo.name
            binding.descriptionTextView.text = repo.description
            binding.starCountTextView.text = repo.startCount.toString()
            binding.forkCountTextView.text = repo.forkCount.toString()

            binding.root.setOnClickListener {
                onClick(repo)
            }
        }


    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoHolder {
        return RepoHolder(
            ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RepoHolder, position: Int) {
        holder.bind(currentList[position])
    }
}