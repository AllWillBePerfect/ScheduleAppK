package com.schedulev2.settings.modal

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.schedulev2.settings.databinding.V2SingleConfigModalBinding
import com.schedulev2.settings.dialogs.replace.adapter.model.ClickableChooseDayDelegate
import com.schedulev2.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedulev2.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class ChangeDaysModal :
    BaseBottomSheetDialogFragment<V2SingleConfigModalBinding>(V2SingleConfigModalBinding::inflate) {

    private val viewModel by activityViewModels<ChangeDaysModalViewModel>()
    private lateinit var adapter: UniversalRecyclerViewAdapter<DayChooseItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {

            adapter = UniversalRecyclerViewAdapter(
                delegates = listOf(ClickableChooseDayDelegate(viewModel::onDayClick)),
                diffUtilCallback = DayChooseItem.DayChooseItemDiffUtil(),
            )
            binding.chooseDayRecyclerView.adapter = adapter
//            adapter.items = listOf(
//                DayChooseItem.ClickableChooseDay("ПН"),
//                DayChooseItem.ClickableChooseDay("ВТ"),
//                DayChooseItem.ClickableChooseDay("СР"),
//                DayChooseItem.ClickableChooseDay("ЧТ"),
//                DayChooseItem.ClickableChooseDay("ПТ"),
//                DayChooseItem.ClickableChooseDay("СБ"),
//            )

            binding.changeButton.setOnClickListener {
                viewModel.saveChanges()
                dismiss()
            }

            viewModel.liveData.observe(viewLifecycleOwner) {
                it.eventForCheck?.let { event ->
                    adapter.items = viewModel.mapList(event.days)
                }
            }


        }
    }

    companion object {
        const val TAG = "ChangeDaysModal"
    }
}