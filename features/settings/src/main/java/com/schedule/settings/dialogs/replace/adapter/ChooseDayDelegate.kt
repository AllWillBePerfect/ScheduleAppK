package com.schedule.settings.dialogs.replace.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedule.settings.databinding.V2ItemChooseDayBinding
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.google.android.material.R

class ChooseDayDelegate(
    private val onItemClickListener: () -> Unit
) : AdapterItemDelegate<DayChooseItem>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == com.schedule.settings.R.id.v2_item_choose_day)
            onItemClickListener.invoke()
    }
    
    override fun forItem(item: DayChooseItem): Boolean = item is DayChooseItem.ChooseDay

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val binding =
            V2ItemChooseDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return ChooseDayViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: ViewHolder,
        item: DayChooseItem,
        payloads: MutableList<Any>
    ) = (viewHolder as ChooseDayViewHolder).bind(
        item as DayChooseItem.ChooseDay,
        viewHolder.itemView.context
    )

    inner class ChooseDayViewHolder(val binding: V2ItemChooseDayBinding) :
        ViewHolder(binding.root) {
        fun bind(item: DayChooseItem.ChooseDay, context: Context) {
            binding.root.text = item.day
            changeBackgroundColor(context, item)
        }

        private fun changeBackgroundColor(
            context: Context,
            item: DayChooseItem.ChooseDay
        ) {
            val textColorTypedValue = TypedValue()
            val textColorTheme = context.theme

            val backgroundColorTypedValue = TypedValue()
            val backgroundColorTheme = context.theme

            val textColorAttr =
                if (item.isSelected) R.attr.colorOnPrimary else R.attr.colorOnSurface
            val backgroundColorAttr =
                R.attr.colorPrimary

            textColorTheme.resolveAttribute(textColorAttr, textColorTypedValue, true)
            backgroundColorTheme.resolveAttribute(
                backgroundColorAttr,
                backgroundColorTypedValue,
                true
            )

            val textColor = if (textColorTypedValue.resourceId != 0)
                ContextCompat.getColor(context, textColorTypedValue.resourceId)
            else textColorTypedValue.data

            val backgroundColor = if (backgroundColorTypedValue.resourceId != 0)
                ContextCompat.getColor(context, backgroundColorTypedValue.resourceId)
            else backgroundColorTypedValue.data

            binding.root.setTextColor(textColor)
            binding.root.setBackgroundColor(
                if (item.isSelected) backgroundColor else context.resources.getColor(
                    android.R.color.transparent
                )
            )
            if (item.isSelected)
                binding.root.background = ContextCompat.getDrawable(
                    context,
                    com.schedule.settings.R.drawable.part_choose_day_bg
                )
            binding.root
        }
    }
}