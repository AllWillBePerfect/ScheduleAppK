package com.schedule.schedule.test_scroll

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.schedule.schedule.databinding.TestScrollFragmentBinding
import com.schedule.schedule.databinding.TestScrollItemBinding
import com.schedule.schedule.databinding.TestScrollItemInnerRecyclerViewBinding
import com.schedule.views.BaseFragment

class TestScrollFragment :
    BaseFragment<TestScrollFragmentBinding>(TestScrollFragmentBinding::inflate) {

    private lateinit var adapter: TestScrollAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TestScrollAdapter()
        binding.viewPager.adapter = adapter
        adapter.items = listOf(TestScrollViewHolderItem("hello"))

        binding.button.setOnClickListener {
            for (i in 0 until adapter.items.size) {
                val viewHolder = adapter.getViewHolder(i)
                if (viewHolder is TestScrollAdapter.TestScrollViewHolder) {
                    viewHolder.binding.recyclerView.post {
//                        viewHolder.binding.recyclerView.smoothScrollToPosition(3)
                        (viewHolder.binding.recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(3, 0)

                    }
                }
            }
        }

    }
}

private class TestScrollAdapter : RecyclerView.Adapter<ViewHolder>() {

    var items: List<TestScrollViewHolderItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val viewHolders = SparseArray<ViewHolder>()
    fun getViewHolder(position: Int): ViewHolder? {
        return viewHolders.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestScrollItemInnerRecyclerViewBinding.inflate(layoutInflater, parent, false)
        return TestScrollViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as TestScrollViewHolder).bind(items[position])
        viewHolders.put(position, holder)
    }

    override fun getItemCount(): Int = items.size

    class TestScrollViewHolder(val binding: TestScrollItemInnerRecyclerViewBinding) :
        ViewHolder(binding.root) {

        val adapter = InnerTestScrollAdapter()

        init {
            binding.recyclerView.adapter = adapter
        }

        fun bind(item: TestScrollViewHolderItem) {
            adapter.items = listOf(
                InnerTestScrollViewHolderItem("1"),
                InnerTestScrollViewHolderItem("2"),
                InnerTestScrollViewHolderItem("3"),
                InnerTestScrollViewHolderItem("4"),
                InnerTestScrollViewHolderItem("5"),
                InnerTestScrollViewHolderItem("6"),
                InnerTestScrollViewHolderItem("7"),
                InnerTestScrollViewHolderItem("8"),
                InnerTestScrollViewHolderItem("9"),
                InnerTestScrollViewHolderItem("10"),
                InnerTestScrollViewHolderItem("11"),
                InnerTestScrollViewHolderItem("12"),
                InnerTestScrollViewHolderItem("13"),
                InnerTestScrollViewHolderItem("14"),
                InnerTestScrollViewHolderItem("15"),
                InnerTestScrollViewHolderItem("16"),
                InnerTestScrollViewHolderItem("17"),
                InnerTestScrollViewHolderItem("18"),
                InnerTestScrollViewHolderItem("19"),
                InnerTestScrollViewHolderItem("20"),
            )

        }
    }
}

data class TestScrollViewHolderItem(val aboba: String)

class InnerTestScrollAdapter() : RecyclerView.Adapter<ViewHolder>() {

    var items: List<InnerTestScrollViewHolderItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TestScrollItemBinding.inflate(layoutInflater, parent, false)
        return InnerTestScrollViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as InnerTestScrollViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class InnerTestScrollViewHolder(val binding: TestScrollItemBinding) : ViewHolder(binding.root) {
        fun bind(item: InnerTestScrollViewHolderItem) {
            binding.root.text = item.number
        }
    }
}

data class InnerTestScrollViewHolderItem(val number: String)