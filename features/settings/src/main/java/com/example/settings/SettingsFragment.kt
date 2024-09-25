package com.example.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import com.example.data.event_manager.RestoreDialogEventManager
import com.example.settings.adapter.PressExpandableOptionDelegate
import com.example.settings.adapter.PressOptionDelegate
import com.example.settings.adapter.SectionDelegate
import com.example.settings.adapter.SwitchOptionDelegate
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.V2FragmentSettingsBinding
import com.example.settings.dialogs.multiple.MultipleOptionDialog
import com.example.settings.dialogs.replace.ReplaceOptionDialog
import com.example.settings.dialogs.single.SingleOptionDialog
import com.example.values.R
import com.example.views.BaseFragment
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment :
    BaseFragment<V2FragmentSettingsBinding>(V2FragmentSettingsBinding::inflate) {


    private val viewModel by activityViewModels<SettingsViewModel>()

    private lateinit var adapter: UniversalRecyclerViewAdapter<SettingsItem>

    @Inject
    lateinit var router: SettingsFragmentContract


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                SectionDelegate(),
                PressOptionDelegate(),
                PressExpandableOptionDelegate(),
                SwitchOptionDelegate(),
            ),
            diffUtilCallback = SettingsItem.SettingsItemDiffUtil()
        )

        binding.recyclerView.adapter = adapter
        adapter.items = createGroupSection() + createStyleSection()
//        + createKillerFeaturesSection()

        viewModel.updateUiLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck.let { adapter.items = createGroupSection() + createStyleSection()
//                + createKillerFeaturesSection()
            }
        }

        viewModel.restoreDialogLiveData.observe(viewLifecycleOwner) {
            it.event?.let {event ->
                when (event) {
                    RestoreDialogEventManager.Companion.RestoreDialogState.Single -> {
                        val dialog = SingleOptionDialog()
                        dialog.show(requireActivity().supportFragmentManager, "singleOption")
                    }
                    RestoreDialogEventManager.Companion.RestoreDialogState.Replace -> {
                        val dialog = ReplaceOptionDialog()
                        dialog.show(requireActivity().supportFragmentManager, "replaceOption")
                    }
                    RestoreDialogEventManager.Companion.RestoreDialogState.Multiple -> {
                        val dialog = MultipleOptionDialog()
                        dialog.show(requireActivity().supportFragmentManager, "multipleOption")
                    }
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            Log.d("backPressed", "from $TAG")
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowTitleEnabled(true)
        val title = "Настройки"
        toolbar?.title = title
    }

    private fun developToast() =
        Toast.makeText(requireContext(), "В разработке*", Toast.LENGTH_SHORT).show()

    private fun setupDynamicColorsDialog() {
        val string = if (viewModel.getDynamicColors()) "Отключить" else "Активировать"
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Dynamic Colors")
            .setMessage("Эта опция активирует/отключает цветовую палитру из акцентных цветов темы устройства")
            .setPositiveButton(string) { _, _ ->
                viewModel.changeDynamicColors()
                router.recreateApp()
            }
            .show()
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
        router.intentReloadApp()

    }

    @DrawableRes
    fun getNightModeDrawable(): Int =
        when (viewModel.getNightMode()) {
            0 -> R.drawable.light_mode
            1 -> R.drawable.dark_mode
            2 -> R.drawable.follow_system_mode
            else -> throw IllegalStateException("Unknown night mode")
        }

    private fun createGroupSection(): List<SettingsItem> = listOf(
        SettingsItem.Section("Общие"),
        SettingsItem.PressOption(
            title = "Одна группа",
            subtitle = "Выберите группу для отображения на экране расписания",
            icon = R.drawable.group,
            cornersType = SettingsItem.CornersType.TOP,
            changeBackgroundColor = viewModel.singleGroupStateCheck()
        ) {
//            router.navigateToAddSingleGroupScreen()
//            val singleOptionConfigModal = SingleOptionConfigModal()
//            singleOptionConfigModal.show(requireActivity().supportFragmentManager, SingleOptionConfigModal.TAG)
            val dialog = SingleOptionDialog()
            dialog.show(requireActivity().supportFragmentManager, "singleOption")
        },
        SettingsItem.PressOption(
            title = "Режим ВПК",
            subtitle = "Выберите несколько групп для отображения на экране расписания",
            icon = R.drawable.groups,
            cornersType = SettingsItem.CornersType.NO_CORNERS,
            changeBackgroundColor = viewModel.replaceGroupStateCheck()
        ) {
//            router.navigateToMultipleGroupOptionFragment()
            val dialog = ReplaceOptionDialog()
            dialog.show(requireActivity().supportFragmentManager, "replaceOption")
        },
        SettingsItem.PressOption(
            title = "Много групп",
            subtitle = "Выберите нужные группы для отображения друг под другом на экране расписания",
            icon = R.drawable.many_groups,
            cornersType = SettingsItem.CornersType.BOTTOM,
            changeBackgroundColor = viewModel.multipleGroupStateCheck()
        ) {
//            developToast()
            val dialog = MultipleOptionDialog()
            dialog.show(requireActivity().supportFragmentManager, "multipleOption")
        },
    )

    private fun performClick(it: SettingsItem.PressExpandableOption) {
        val list = adapter.items.toMutableList()
        val item = it.copy(isExpand = !it.isExpand)
        list[adapter.items.indexOf(it)] = item
        adapter.items = list
    }

    private fun createStyleSection(): List<SettingsItem> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            listOf(
                SettingsItem.Section("Оформление"),
                SettingsItem.SwitchOption(
                    title = "Dynamic colors",
                    subtitle = "Активировируйте динамические цвета",
                    icon = R.drawable.palette,
                    isChecked = viewModel.getDynamicColors(),
                    cornersType = SettingsItem.CornersType.TOP,
                    action = ::setupDynamicColorsDialog
                ),
                SettingsItem.PressOption(
                    title = "Тема приложения",
                    subtitle = "Выберите тему приложения",
                    icon = getNightModeDrawable(),
                    cornersType = SettingsItem.CornersType.BOTTOM
                ) { setupNightModeDialog() },
            )
        else listOf(
            SettingsItem.Section("Оформление"),
            SettingsItem.PressOption(
                title = "Тема приложения",
                subtitle = "Выберите тему приложения",
                icon = getNightModeDrawable(),
                cornersType = SettingsItem.CornersType.SINGLE
            ) { setupNightModeDialog() },
        )


    private fun createKillerFeaturesSection(): List<SettingsItem> = listOf(
        SettingsItem.Section("Определенно киллер фичи ☜(ﾟヮﾟ☜)"),
        SettingsItem.PressOption(
            title = "Цитаты Стейтема",
            subtitle = "Каждые день будет отображаться случайная цитата Стейтема из его золотой коллекции цитат",
            icon = R.drawable.mood,
            cornersType = SettingsItem.CornersType.TOP,
            action = ::developToast
        ),
        SettingsItem.PressOption(
            title = "Бизнес-ланчи в сфеду кафе",
            subtitle = "Информация о бизнес-ланчах в сфеду кафе по будням",
            icon = R.drawable.food,
            cornersType = SettingsItem.CornersType.BOTTOM,
            action = ::developToast
        ),
    )


    companion object {
        private const val CORNER_SIZE_VALUE = 40F

        private const val TAG = "SettingsFragment"
    }
}