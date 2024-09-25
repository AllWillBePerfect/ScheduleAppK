package com.schedule.settings.dialogs.replace.adapter.model

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedule.settings.databinding.V2ItemChooseDayBigBinding
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.google.android.material.R

class ClickableChooseDayDelegate(private val onItemClickListener: (DayChooseItem.ClickableChooseDay) -> Unit) : AdapterItemDelegate<DayChooseItem>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == com.schedule.settings.R.id.v2_item_choose_day_big) {
            val item = p0.tag as DayChooseItem.ClickableChooseDay
            onItemClickListener.invoke(item)
        }
    }

    override fun forItem(item: DayChooseItem): Boolean = item is DayChooseItem.ClickableChooseDay

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val binding =
            V2ItemChooseDayBigBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return ClickableChooseDayViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: ViewHolder,
        item: DayChooseItem,
        payloads: MutableList<Any>
    ) = (viewHolder as ClickableChooseDayViewHolder).bind(
        item as DayChooseItem.ClickableChooseDay,
        viewHolder.itemView.context
    )

    inner class ClickableChooseDayViewHolder(val binding: V2ItemChooseDayBigBinding) :
        ViewHolder(binding.root) {
        fun bind(item: DayChooseItem.ClickableChooseDay, context: Context) {
            binding.root.text = item.day
            changeBackgroundColor(context, item)
            binding.root.tag = item
        }

        private fun changeBackgroundColor(
            context: Context,
            item: DayChooseItem.ClickableChooseDay
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