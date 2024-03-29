package com.example.enter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.enter.databinding.ItemChooseGroupBinding
import com.example.enter.R

class GroupChooseAdapter(
    private val onItemClickListener: ((GroupItem) -> Unit)
) : ListAdapter<GroupItem, GroupChooseAdapter.GroupItemViewHolder>(ChooseGroupItemCallback()),
    View.OnClickListener {

    override fun onClick(v: View?) {
        if (v?.id == R.id.group_choose_item) {
            val item = (v.tag as GroupItem)
            onItemClickListener(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupItemViewHolder {
        val binding =
            ItemChooseGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return GroupItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GroupItemViewHolder(val binding: ItemChooseGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(groupItem: GroupItem) {
            binding.groupChooseItem.text = groupItem.groupName
            binding.root.tag = groupItem
        }
    }
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