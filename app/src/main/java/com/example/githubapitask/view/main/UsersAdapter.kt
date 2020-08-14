package com.example.githubapitask.view.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapitask.R
import com.example.githubapitask.databinding.ItemUserBinding
import com.example.githubapitask.model.entity.UserModel
import com.example.githubapitask.model.entity.UserModelDiffUtils

class UsersAdapter :
    PagedListAdapter<UserModel, UsersAdapter.UserViewHolder>(UserModelDiffUtils) {

    private val tag = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val viewBinding = DataBindingUtil.inflate<ItemUserBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false
        )
        return UserViewHolder(viewBinding.root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val data = getItem(position)
        val viewBinding = DataBindingUtil.getBinding<ItemUserBinding>(holder.itemView)
        viewBinding?.let {
            it.data = data
        }
    }

    class UserViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}