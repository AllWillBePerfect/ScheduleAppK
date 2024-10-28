package com.schedulev2.settings.dialogs.single

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.schedulev2.settings.databinding.V2SingleOptionDialogBinding
import com.schedulev2.settings.dialogs.BaseDialogFragment
import com.schedulev2.settings.dialogs.adapter.SingleItemDelegate
import com.schedulev2.settings.dialogs.adapter.model.GroupItem
import com.schedulev2.settings.dialogs.swipe.SingleDialogSwipe
import com.schedulev2.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class SingleOptionDialog :
    BaseDialogFragment<V2SingleOptionDialogBinding>(V2SingleOptionDialogBinding::inflate) {
    private val viewModel by activityViewModels<SingleOptionViewModel>()

    private lateinit var adapter: UniversalRecyclerViewAdapter<GroupItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                SingleItemDelegate(::switchGroup)
            ),
            diffUtilCallback = GroupItem.GroupItemDiffUtil()
        )
        dialogBinding.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = adapter

        dialogBinding.navigate.setOnClickListener {
            viewModel.navigate()
            dialog?.dismiss()
        }

        val itemTouchHelper =
            ItemTouchHelper(SingleDialogSwipe(adapter, requireContext(), viewModel::deleteGroup))
        itemTouchHelper.attachToRecyclerView(dialogBinding.recyclerView)

        viewModel.adapterListLiveData.observe(viewLifecycleOwner) {
            adapter.items =
                it.map { groupName ->
                    GroupItem.Single(
                        groupName = groupName,
                        isSelected = viewModel.isSelected(groupName)
                    )
                }
        }



        viewModel.setList()

    }

    private fun switchGroup(group: GroupItem.Single) = viewModel.switchGroup(group)
}