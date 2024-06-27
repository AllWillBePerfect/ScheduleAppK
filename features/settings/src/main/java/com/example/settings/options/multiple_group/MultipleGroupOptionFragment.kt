package com.example.settings.options.multiple_group

import com.example.settings.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.settings.databinding.FragmentOptionMultipleGroupBinding
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultipleGroupOptionFragment : Fragment() {

    private var _binding: FragmentOptionMultipleGroupBinding? = null
    private val binding: FragmentOptionMultipleGroupBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOptionMultipleGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupToggleButtons()
        setupToggleButtonsWeeks()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowTitleEnabled(true)
        val title = "Несколько групп *Developing"
        toolbar?.title = title
    }

    private fun setupToggleButtons() {
        binding.configurationTitle.title.text = "Конфигурация"

        binding.toggleGroup.isSingleSelection = true

        binding.toggleGroup.isSelectionRequired = true
        binding.toggleGroup.check(R.id.sharedOptionButton)
        binding.toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked)
                when (checkedId) {
                    R.id.sharedOptionButton -> Toast.makeText(
                        requireContext(),
                        "0",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.individualOptionButton -> Toast.makeText(
                        requireContext(),
                        "1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun setupToggleButtonsWeeks() {
        binding.toggleGroupDays.isSelectionRequired = true
        binding.toggleGroupDays.check(R.id.monday_option)
        binding.toggleGroupDays.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.monday_option -> Toast.makeText(requireContext(), "0", Toast.LENGTH_SHORT)
                        .show()

                    R.id.tuesday_option -> Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT)
                        .show()

                    R.id.wednesday_option -> Toast.makeText(
                        requireContext(),
                        "1",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.thursday_option -> Toast.makeText(
                        requireContext(),
                        "1",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.friday_option -> Toast.makeText(requireContext(), "1", Toast.LENGTH_SHORT)
                        .show()

                    R.id.saturday_option -> Toast.makeText(
                        requireContext(),
                        "1",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}