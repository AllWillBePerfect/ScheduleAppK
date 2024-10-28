package com.schedulev2.schedule.v2.adapter.viewpager.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedulev2.schedule.databinding.V2ItemRecyclerViewDayBinding
import com.schedulev2.schedule.v2.adapter.viewpager.RecyclerViewDayCurrentDelegate
import com.schedulev2.schedule.v2.adapter.viewpager.RecyclerViewDayDelegate
import com.schedulev2.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedulev2.views.adapter.adaptersdelegate.DelegateDiffResult

class RecyclerViewDayAdapter : RecyclerView.Adapter<ViewHolder>() {

    var items: List<ViewPagerItem> = emptyList()
        get() = field
        set(value) {
            val sd = DelegateDiffResult(ViewPagerItem.ViewPagerItemDiffUtil())
            val diffResult = DiffUtil.calculateDiff(sd.getCallback(field, value))
            field = value
//            notifyDataSetChanged()
            diffResult.dispatchUpdatesTo(this)
        }

    private val viewHolders = SparseArray<ViewHolder>()
    fun getViewHolder(position: Int): ViewHolder? {
        return viewHolders.get(position)
    }

    override fun getItemCount(): Int = items.size


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewHolder = when (viewType) {
            0 -> RecyclerViewDayDelegate.RecyclerViewDayViewHolder(
                V2ItemRecyclerViewDayBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            1 -> RecyclerViewDayCurrentDelegate.RecyclerViewDayCurrentViewHolder(
                V2ItemRecyclerViewDayBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalStateException("Wrong ViewType from RecyclerViewDayAdapter")
        }
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        when (holder) {
            is RecyclerViewDayDelegate.RecyclerViewDayViewHolder -> {
                holder.bind(items[position] as ViewPagerItem.RecyclerViewDay)
                viewHolders.put(position, holder)
            }

            is RecyclerViewDayCurrentDelegate.RecyclerViewDayCurrentViewHolder -> {
                holder.bind(items[position] as ViewPagerItem.RecyclerViewCurrentDay)
                viewHolders.put(position, holder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ViewPagerItem.RecyclerViewDay -> 0
            is ViewPagerItem.RecyclerViewCurrentDay -> 1
        }
    }

}