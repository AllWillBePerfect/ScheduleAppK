package com.example.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.PartPressOptionBinding
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.google.android.material.shape.ShapeAppearanceModel

class PressOptionDelegate : AdapterItemDelegate<SettingsItem> {
    override fun forItem(item: SettingsItem): Boolean = item is SettingsItem.PressOption

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            PartPressOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressOptionViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: SettingsItem,
        payloads: MutableList<Any>
    ) = (viewHolder as PressOptionViewHolder).bind(item as SettingsItem.PressOption)


    inner class PressOptionViewHolder(private val binding: PartPressOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem.PressOption) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            binding.icon.setImageResource(item.icon)
            binding.root.shapeAppearanceModel =
                SettingsItem.setupCornersToMaterialCardView(item.cornersType, binding.root)
            binding.root.setOnClickListener { item.action.invoke() }
        }


    }
}