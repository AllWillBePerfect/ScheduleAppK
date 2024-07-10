package com.example.clear

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.clear.databinding.FragmentClearBinding
import com.example.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClearFragment : BaseFragment<FragmentClearBinding>(FragmentClearBinding::inflate) {

    private val viewModel by activityViewModels<ClearFragmentViewModel>()

    @Inject
    lateinit var router: ClearFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clearButton.setOnClickListener(::clearData)
        viewModel.clearStateLiveData.observe(viewLifecycleOwner, ::clearStateObserve)
    }

    private fun clearData(view: View) = viewModel.clearData()
    private fun clearStateObserve(clearState: ClearFragmentViewModel.ClearState) {
        when (clearState) {
            is ClearFragmentViewModel.ClearState.Success -> {
                router.launchAuthScreen()
            }

            is ClearFragmentViewModel.ClearState.Error -> {
                Log.d("ClearFragment", clearState.error.toString() + " --- " + clearState.message)
                binding.clearButton.isEnabled = false
                binding.errorText.visibility = View.VISIBLE
            }

            is ClearFragmentViewModel.ClearState.Loading -> {
                if (clearState.state) {
                    binding.clearButton.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.clearButton.visibility = View.VISIBLE
                }
            }
        }
    }
}