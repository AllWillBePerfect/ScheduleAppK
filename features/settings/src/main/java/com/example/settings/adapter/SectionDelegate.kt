package com.example.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.PartSectionBinding
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

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