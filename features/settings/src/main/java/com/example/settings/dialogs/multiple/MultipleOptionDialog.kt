package com.example.settings.dialogs.multiple

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.settings.databinding.V2MultipleOptionDialogBinding
import com.example.settings.dialogs.BaseDialogFragment
import com.example.settings.dialogs.adapter.MultipleItemDelegate
import com.example.settings.dialogs.adapter.model.GroupItem
import com.example.settings.dialogs.swipe.MultipleDialogSwipe
import com.example.settings.dialogs.swipe.SingleDialogSwipe
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class MultipleOptionDialog : BaseDialogFragment<V2MultipleOptionDialogBinding>(
    V2MultipleOptionDialogBinding::inflate
) {

    private val viewModel by activityViewModels<MultipleOptionViewModel>()

    private lateinit var adapter: UniversalRecyclerViewAdapter<GroupItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                MultipleItemDelegate {}
            ),
            diffUtilCallback = GroupItem.GroupItemDiffUtil()
        )

        dialogBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = adapter

        dialogBinding.activate.setOnClickListener { viewModel.switchGroup() }
        dialogBinding.navigate.setOnClickListener {
            viewModel.navigate()
            dialog?.dismiss()
        }

        val itemTouchHelper =
            ItemTouchHelper(MultipleDialogSwipe(adapter, requireContext(), viewModel::deleteGroup, viewModel::moveGroups))
        itemTouchHelper.attachToRecyclerView(dialogBinding.recyclerView)

        viewModel.adapterListLiveData.observe(viewLifecycleOwner) {
            adapter.items =
                it.map { groupName ->
                    GroupItem.Multiple(
                        groupName = groupName,
                        isSelected = viewModel.isSelected(groupName)
                    )
                }
            dialogBinding.activate.isEnabled = it.isNotEmpty()
        }


        viewModel.setList()

    }
}