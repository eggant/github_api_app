package com.example.githubapitask.model.entity

import androidx.recyclerview.widget.DiffUtil

data class UserQueryResult(
    val total_count: Int = 0,
    val incomplete_results: Boolean = false,
    val items: List<UserModel> = emptyList()
)

data class UserModel(

    val login: String,
    val id: Int,
    val node_id: String,
    val avatar_url: String,
    val gravatar_id: String,
    val url: String,
    val html_url: String,
    val followers_url: String,
    val subscriptions_url: String,
    val organizations_url: String,
    val repos_url: String,
    val received_events_url: String,
    val type: String,
    val score: Int
)

object UserModelDiffUtils : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        return oldItem == newItem
    }
}