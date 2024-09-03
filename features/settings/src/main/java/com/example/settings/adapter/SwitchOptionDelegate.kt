package com.example.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.PartPressOptionBinding
import com.example.settings.databinding.V2SwitchOptionBinding
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.google.android.material.shape.ShapeAppearanceModel

class SwitchOptionDelegate : AdapterItemDelegate<SettingsItem> {
    override fun forItem(item: SettingsItem): Boolean = item is SettingsItem.SwitchOption

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            V2SwitchOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SwitchOptionViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: SettingsItem,
        payloads: MutableList<Any>
    ) = (viewHolder as SwitchOptionViewHolder).bind(item as SettingsItem.SwitchOption)


    inner class SwitchOptionViewHolder(private val binding: V2SwitchOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem.SwitchOption) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            binding.icon.setImageResource(item.icon)
            binding.switchWidget.isChecked = item.isChecked
            binding.root.shapeAppearanceModel = SettingsItem.setupCornersToMaterialCardView(item.cornersType, binding.root)
            binding.root.setOnClickListener { item.action.invoke() }
        }


    }
}