package com.schedule.settings.dialogs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.schedule.settings.databinding.V2ItemReplaceBinding
import com.schedule.settings.dialogs.adapter.model.GroupItem
import com.schedule.settings.dialogs.replace.adapter.ChooseDayDelegate
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedule.views.adapter.adaptersdelegate.AdapterItemDelegate
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class ReplaceItemDelegate(private val onItemClickListener: (GroupItem.Replace) -> Unit) : AdapterItemDelegate<GroupItem>, OnClickListener {

    override fun onClick(p0: View?) {
        if (p0?.id == com.schedule.settings.R.id.v2_item_replace) {
            val item = p0.tag as GroupItem.Replace
            onItemClickListener.invoke(item)
        }

    }

    override fun forItem(item: GroupItem): Boolean = item is GroupItem.Replace

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            V2ItemReplaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.root.setOnClickListener(this)
        return ReplaceItemViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: GroupItem,
        payloads: MutableList<Any>
    ) = (viewHolder as ReplaceItemViewHolder).bind(item as GroupItem.Replace)

    inner class ReplaceItemViewHolder(private val binding: V2ItemReplaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
            var adapter: UniversalRecyclerViewAdapter<DayChooseItem> = UniversalRecyclerViewAdapter(
                delegates = listOf(
                    ChooseDayDelegate{onItemClickListener.invoke(binding.root.tag as GroupItem.Replace)}
                ),
                diffUtilCallback = DayChooseItem.DayChooseItemDiffUtil()
            )

            init {
                binding.daysRecyclerView.layoutManager = object : LinearLayoutManager(itemView.context) {
                    override fun canScrollVertically(): Boolean = false

                    override fun canScrollHorizontally(): Boolean = false

                    override fun setOrientation(orientation: Int) {
                        super.setOrientation(RecyclerView.HORIZONTAL)
                    }
                }
                binding.daysRecyclerView.adapter = adapter
            }

        fun bind(item: GroupItem.Replace) {
            binding.groupName.text = if (item.isSelected) "* " + item.groupName else item.groupName
            binding.vpkName.text = item.vpkName
            adapter.items = mapList(item.replacedDays)
            binding.daysRecyclerView.visibility = if (item.isShowDays) View.VISIBLE else View.GONE
            binding.root.tag = item

        }
    }

    companion object {
        fun mapList(list: List<Int>): List<DayChooseItem.ChooseDay> {
            val listItems: MutableList<DayChooseItem.ChooseDay> = mutableListOf()
            for (i in 1..6) {
                listItems.add(DayChooseItem.ChooseDay(mapIntToString(i), list.contains(i)))
            }
            return listItems
        }

        private fun mapIntToString(value: Int): String {
            return when (value) {
                1 -> "ПН"
                2 -> "ВТ"
                3 -> "СР"
                4 -> "ЧТ"
                5 -> "ПТ"
                6 -> "СБ"
                else -> throw IllegalArgumentException("Wrong day")
            }
        }
    }
}