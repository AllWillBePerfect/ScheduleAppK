package com.schedule.schedule.v2.container.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedule.schedule.databinding.V2ItemDrawerTitleBinding
import com.schedule.schedule.v2.container.adapter.model.SettingsDrawerItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

class TitleDelegate(
    private val onItemClick: () -> Unit
) : AdapterItemDelegate<SettingsDrawerItem>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == com.schedule.schedule.R.id.title) onItemClick.invoke()
    }

    override fun forItem(item: SettingsDrawerItem): Boolean = item is SettingsDrawerItem.Title

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = V2ItemDrawerTitleBinding.inflate(layoutInflater, parent, false)
        binding.root.setOnClickListener(this)
        return TitleViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: ViewHolder,
        item: SettingsDrawerItem,
        payloads: MutableList<Any>
    ) {
        (viewHolder as TitleViewHolder).bind(item as SettingsDrawerItem.Title)
    }

    class TitleViewHolder(val binding: V2ItemDrawerTitleBinding) : ViewHolder(binding.root) {
        fun bind(item: SettingsDrawerItem.Title) {
            binding.title.text = item.title
        }
    }


}