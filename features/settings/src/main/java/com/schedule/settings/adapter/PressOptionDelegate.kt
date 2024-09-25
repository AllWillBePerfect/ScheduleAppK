package com.schedule.settings.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.schedule.settings.adapter.model.SettingsItem
import com.schedule.settings.databinding.PartPressOptionBinding
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate

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
    ) = (viewHolder as PressOptionViewHolder).bind(
        item as SettingsItem.PressOption,
        viewHolder.itemView.context
    )


    inner class PressOptionViewHolder(private val binding: PartPressOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem.PressOption, context: Context) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            binding.icon.setImageResource(item.icon)
            binding.root.shapeAppearanceModel =
                SettingsItem.setupCornersToMaterialCardView(item.cornersType, binding.root)
            changeBackgroundColor(context, item.changeBackgroundColor)
            binding.root.setOnClickListener { item.action.invoke() }

        }

        private fun changeBackgroundColor(context: Context, changeColor: Boolean) {
            val typedValue = TypedValue()
            val theme = context.theme
            val attr =
                if (changeColor) com.google.android.material.R.attr.colorSurfaceContainerHighest else com.google.android.material.R.attr.colorSurfaceContainer
            theme.resolveAttribute(
                attr,
                typedValue,
                true
            )
            val color = if (typedValue.resourceId != 0)
                ContextCompat.getColor(context, typedValue.resourceId)
            else typedValue.data

            binding.root.setCardBackgroundColor(color)
        }

    }
}