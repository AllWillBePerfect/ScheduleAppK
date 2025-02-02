package com.schedulev2.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.schedulev2.settings.adapter.model.SettingsItem
import com.schedulev2.settings.databinding.PartSectionBinding
import com.schedulev2.views.adapter.adaptersdelegate.AdapterItemDelegate

class SectionDelegate : AdapterItemDelegate<SettingsItem> {
    override fun forItem(item: SettingsItem): Boolean = item is SettingsItem.Section

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = PartSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: SettingsItem,
        payloads: MutableList<Any>
    ) = (viewHolder as SectionViewHolder).bind(item as SettingsItem.Section)


    inner class SectionViewHolder(private val binding: PartSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem.Section) {
            binding.title.text = item.title
        }
    }
}