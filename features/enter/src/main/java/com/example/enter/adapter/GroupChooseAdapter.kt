package com.example.enter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.enter.databinding.ItemChooseGroupBinding

class GroupChooseAdapter : ListAdapter<GroupItem, GroupChooseAdapter.GroupItemViewHolder>(ChooseGroupItemCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemViewHolder {
        val binding = ItemChooseGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupItemViewHolder, position: Int) {
    }

    class GroupItemViewHolder(val binding: ItemChooseGroupBinding) : RecyclerView.ViewHolder(binding.root)
}

data class GroupItem(
    val groupName: String,
    val groupHtm: String
)

class ChooseGroupItemCallback : DiffUtil.ItemCallback<GroupItem>() {
    override fun areItemsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: GroupItem, newItem: GroupItem): Boolean {
        return oldItem.groupName == newItem.groupName
    }
}