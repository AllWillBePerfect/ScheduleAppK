package com.example.schedule.v2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.schedule.databinding.FragmentScheduleBinding
import com.example.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragmentV2: BaseFragment<FragmentScheduleBinding>(FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}