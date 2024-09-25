package com.schedule.rxtest.sharednumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.schedule.rxtest.R
import com.schedule.rxtest.databinding.FragmentValueBinding

class NumberFragment : Fragment() {

    var _binding: FragmentValueBinding? = null
    val binding get() = _binding!!

    val viewModel: NumberViewModel by activityViewModels<NumberViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            viewModel.addNumber(1)
        }

        binding.clearButton.setOnClickListener {
            viewModel.clearNumber()
        }

        binding.navigateButton.setOnClickListener {
            launchAddFragment()
        }

        viewModel.getSelectedNumbers().observe(viewLifecycleOwner) {numbers ->
            numbers?.let {
                if (numbers.isNotEmpty()) {
                    binding.number.text = numbers.sum().toString()
                } else {
                    binding.number.text = "empty"
                }
            }
        }

    }

    private fun launchAddFragment() {
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.child_container, AddFragment())
            addToBackStack(null)
            commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}