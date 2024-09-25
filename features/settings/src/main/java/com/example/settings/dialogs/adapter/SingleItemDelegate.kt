package com.example.settings.dialogs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.databinding.V2ItemSingleBinding
import com.example.settings.dialogs.adapter.model.GroupItem
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class SingleItemDelegate(
    private val onItemClickListener: ((GroupItem.Single) -> Unit)

) : AdapterItemDelegate<GroupItem>, OnClickListener {
    override fun forItem(item: GroupItem): Boolean = item is GroupItem.Single

    override fun onClick(p0: View?) {
        if (p0?.id == com.example.settings.R.id.v2_item_single)
            onItemClickListener.invoke(p0.tag as GroupItem.Single)
    }

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            V2ItemSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return SingleItemViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: GroupItem,
        payloads: MutableList<Any>
    ) = (viewHolder as SingleItemViewHolder).bind(item as GroupItem.Single)

    inner class SingleItemViewHolder(private val binding: V2ItemSingleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GroupItem.Single) {
            binding.groupName.text = if (item.isSelected) "* " + item.groupName else item.groupName
            binding.root.tag = item
        }
    }

}