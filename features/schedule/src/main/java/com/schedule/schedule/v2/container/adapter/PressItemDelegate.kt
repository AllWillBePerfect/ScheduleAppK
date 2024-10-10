package com.schedule.schedule.v2.container.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedule.schedule.databinding.V2ItemDrawerPressBinding
import com.schedule.schedule.v2.container.adapter.model.SettingsDrawerItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

class PressItemDelegate(
    private val onItemClick: () -> Unit
) : AdapterItemDelegate<SettingsDrawerItem>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == com.schedule.schedule.R.id.pressItem) onItemClick.invoke()

    }

    override fun forItem(item: SettingsDrawerItem): Boolean = item is SettingsDrawerItem.PressItem

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = V2ItemDrawerPressBinding.inflate(layoutInflater, parent, false)
        binding.root.setOnClickListener(this)
        return PressItemViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: ViewHolder,
        item: SettingsDrawerItem,
        payloads: MutableList<Any>
    ) {
        (viewHolder as PressItemViewHolder).bind(item as SettingsDrawerItem.PressItem)
    }

    class PressItemViewHolder(val binding: V2ItemDrawerPressBinding): ViewHolder(binding.root) {
        fun bind(item: SettingsDrawerItem.PressItem) {
            binding.image.setImageResource(item.iconRes)
            binding.name.text = item.title
            binding.root.setOnClickListener {item.onItemClick.invoke()}
        }
    }


}