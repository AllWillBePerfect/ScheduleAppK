package com.example.settings

import android.app.UiModeManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import com.example.settings.adapter.SectionDelegate
import com.example.settings.adapter.PressOptionDelegate
import com.example.settings.adapter.SwitchOptionDelegate
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.V2FragmentSettingsBinding
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
                SwitchOptionDelegate(),
            ),
            diffUtilCallback = SettingsItem.SettingsItemDiffUtil()
        )

        binding.recyclerView.adapter = adapter
        adapter.items = createFirstSection() + createSecondSection() + createThirdSection()
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
            .setTitle("DynamicColors")
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

    private fun createFirstSection(): List<SettingsItem> = listOf(
        SettingsItem.Section("Общие"),
        SettingsItem.PressOption(
            "Одна группа",
            "Выберите группу для отображения на экране расписания",
            R.drawable.group,
            SettingsItem.CornersType.TOP
        ) { router.navigateToAddSingleGroupScreen() },
        SettingsItem.PressOption(
            "Режим ВПК",
            "Выберите несколько групп для отображения на экране расписания",
            R.drawable.groups,
            SettingsItem.CornersType.NO_CORNERS
        ) { router.navigateToMultipleGroupOptionFragment() },
        SettingsItem.PressOption(
            "Много групп",
            "Выберите нужные группы для отображения друг под другом на экране расписания",
            R.drawable.many_groups,
            SettingsItem.CornersType.BOTTOM,
            ::developToast
        ),
    )

    private fun createSecondSection(): List<SettingsItem> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            listOf(
                SettingsItem.Section("Оформление"),
                SettingsItem.SwitchOption(
                    "Dynamic colors",
                    "Активировируйте динамические цвета",
                    R.drawable.palette,
                    viewModel.getDynamicColors(),
                    SettingsItem.CornersType.TOP,
                    ::setupDynamicColorsDialog
                ),
                SettingsItem.PressOption(
                    "Тема приложения",
                    "Выберите тему приложения",
                    getNightModeDrawable(),
                    SettingsItem.CornersType.BOTTOM
                ) { setupNightModeDialog() },
            )
        else listOf(
            SettingsItem.PressOption(
                "Тема приложения",
                "Выберите тему приложения",
                getNightModeDrawable(),
                SettingsItem.CornersType.SINGLE
            ) { setupNightModeDialog() },
        )


    private fun createThirdSection(): List<SettingsItem> = listOf(
        SettingsItem.Section("Определенно киллер фичи ☜(ﾟヮﾟ☜)"),
        SettingsItem.PressOption(
            "Цитаты Стейтема",
            "Каждые день будет отображаться случайная цитата Стейтема из его золотой коллекции цитат",
            R.drawable.mood,
            SettingsItem.CornersType.TOP,
            ::developToast
        ),
        SettingsItem.PressOption(
            "Бизнес-ланчи в сфеду кафе",
            "Информация о бизнес-ланчах в сфеду кафе по будням",
            R.drawable.food,
            SettingsItem.CornersType.BOTTOM,
            ::developToast
        ),
    )


    companion object {
        private const val CORNER_SIZE_VALUE = 40F
    }
}