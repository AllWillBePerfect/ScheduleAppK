package com.example.settings.dialogs.replace

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.databinding.V2ReplaceOptionDialogBinding
import com.example.settings.dialogs.BaseDialogFragment
import com.example.settings.dialogs.adapter.ReplaceItemDelegate
import com.example.settings.dialogs.adapter.model.GroupItem
import com.example.settings.dialogs.replace.adapter.model.ClickableChooseDayDelegate
import com.example.settings.dialogs.replace.adapter.model.DayChooseItem
import com.example.settings.dialogs.swipe.ReplaceDialogSwipe
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class ReplaceOptionDialog :
    BaseDialogFragment<V2ReplaceOptionDialogBinding>(V2ReplaceOptionDialogBinding::inflate) {

    private lateinit var daysAdapter: UniversalRecyclerViewAdapter<DayChooseItem>
    private lateinit var adapter: UniversalRecyclerViewAdapter<GroupItem>

    private val viewModel by activityViewModels<ReplaceOptionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogBinding.toggleGroup.setSingleSelection(true)
        dialogBinding.toggleGroup.isSelectionRequired = true
        val id =
            if (viewModel.getIsGlobalState()) com.example.settings.R.id.sharedOptionButton else com.example.settings.R.id.individualOptionButton
        dialogBinding.toggleGroup.check(id)

        dialogBinding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked)
                when (checkedId) {
                    com.example.settings.R.id.sharedOptionButton -> {
                        viewModel.changeIsGlobalState(true)
                    }

                    com.example.settings.R.id.individualOptionButton -> {
                        viewModel.changeIsGlobalState(false)
                    }
                }
        }


        daysAdapter = UniversalRecyclerViewAdapter(
            delegates = listOf(ClickableChooseDayDelegate(viewModel::onDayClick)),
            diffUtilCallback = DayChooseItem.DayChooseItemDiffUtil()
        )
        dialogBinding.chooseDayRecyclerView.layoutManager = object : LinearLayoutManager(requireContext()) {
            override fun setOrientation(orientation: Int) {
                super.setOrientation(RecyclerView.HORIZONTAL)
            }
        }
        dialogBinding.chooseDayRecyclerView.adapter = daysAdapter


        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                ReplaceItemDelegate(viewModel::switchGroup)
            ),
            diffUtilCallback = GroupItem.GroupItemDiffUtil()
        )
        dialogBinding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        dialogBinding.recyclerView.adapter = adapter

        dialogBinding.navigate.setOnClickListener {
            viewModel.navigate()
            dialog?.dismiss()
        }

        val itemTouchHelper = ItemTouchHelper(ReplaceDialogSwipe(adapter, requireContext(), viewModel::deleteGroup))
        itemTouchHelper.attachToRecyclerView(dialogBinding.recyclerView)


        viewModel.updateUiLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { config ->
                dialogBinding.chooseDayRecyclerView.visibility =
                    if (config.isShared) View.VISIBLE else View.GONE
                adapter.items = config.adapterList
            }
        }

        viewModel.replaceDaysLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { list ->
                daysAdapter.items = list
            }
        }

        viewModel.setReplaceDaysLiveData()
        viewModel.setList()

    }
}