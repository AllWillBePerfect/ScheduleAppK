package com.example.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.settings.databinding.FragmentSettingsBinding
import com.example.values.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    private val viewModel by activityViewModels<SettingsViewModel>()

    @Inject
    lateinit var router: SettingsFragmentContract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        initSettings()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowTitleEnabled(true)
        val title = "Настройки"
        toolbar?.title = title
    }

    private fun initSettings() {

        /*single group*/
        binding.groupSection.title.text = "Общее"

        binding.groupChooseOption.title.text = "Одна группа"
        binding.groupChooseOption.subtitle.text =
            "Нажмите, чтобы выбрать отображаемую группу на экране расписания"
        binding.groupChooseOption.icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.group
            )
        )

        binding.groupChooseOption.root.setOnClickListener {
            router.navigateToAddSingleGroupScreen()
        }

    /*multiple group*/
        binding.groupsChooseOption.title.text = "Несколько групп"
        binding.groupsChooseOption.subtitle.text =
            "Нажмите, чтобы выбрать несколько отображаемых групп на экране расписания"
        binding.groupsChooseOption.icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.groups
            )
        )

        binding.groupsChooseOption.root.setOnClickListener {
            router.navigateToMultipleGroupOptionFragment()
        }

        binding.groupChooseOption.root.shapeAppearanceModel =
            binding.groupChooseOption.root.shapeAppearanceModel.toBuilder()
                .setTopLeftCornerSize(CORNER_SIZE_VALUE)
                .setTopRightCornerSize(CORNER_SIZE_VALUE)
                .build()

        binding.groupsChooseOption.root.shapeAppearanceModel =
            binding.groupsChooseOption.root.shapeAppearanceModel.toBuilder()
                .setBottomLeftCornerSize(CORNER_SIZE_VALUE)
                .setBottomRightCornerSize(CORNER_SIZE_VALUE)
                .build()

        binding.personalSection.title.text = "Оформление"

        /*night mode*/
        binding.nightModeOption.title.text = "Тема приложения"
        binding.nightModeOption.subtitle.text = "Нажмите, чтобы выбрать тему приложения"
        binding.nightModeOption.icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.dark_mode
            )
        )

        binding.nightModeOption.root.setOnClickListener {
            setupNightModeDialog()
        }

        binding.nightModeOption.root.shapeAppearanceModel =
            binding.nightModeOption.root.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(CORNER_SIZE_VALUE)
                .build()
    }

    private fun setupNightModeDialog() {
        val options = arrayOf("Светлая", "Темная", "Системная")
        MaterialAlertDialogBuilder(requireContext())
            .setSingleChoiceItems(options, viewModel.getNightMode()) { dialog, which ->
                when (which) {
                    0 -> {
                        setNightMode(0)
                    }

                    1 -> {
                        setNightMode(1)
                    }

                    2 -> {
                        setNightMode(2)
                    }
                }
            }
            .setTitle("Тема приложения")
            .show()
    }

    private fun setNightMode(modeState: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val uiModeManager =
                requireActivity().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            when (modeState) {
                0 -> uiModeManager.setApplicationNightMode(
                    UiModeManager.MODE_NIGHT_NO
                )

                1 -> uiModeManager.setApplicationNightMode(
                    UiModeManager.MODE_NIGHT_YES
                )

                2 -> uiModeManager.setApplicationNightMode(
                    UiModeManager.MODE_NIGHT_AUTO
                )
            }
        } else {
            when (modeState) {
                0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                2 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
        viewModel.setNightMode(modeState)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CORNER_SIZE_VALUE = 40F
    }
}