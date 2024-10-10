package com.schedule.schedule.v2.container

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.navigation.NavigationView
import com.schedule.schedule.databinding.V2DrawerLayoutHeaderBinding
import com.schedule.schedule.databinding.V2PartNavigationDrawerContainerFragmentBinding
import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.schedule.v2.ScheduleFragmentContractV2
import com.schedule.schedule.v2.ScheduleFragmentV2V2
import com.schedule.schedule.v2.container.adapter.PressItemDelegate
import com.schedule.schedule.v2.container.adapter.TitleDelegate
import com.schedule.schedule.v2.container.adapter.model.SettingsDrawerItem
import com.schedule.values.R
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavigationDrawerContainerFragment :
    BaseFragment<V2PartNavigationDrawerContainerFragmentBinding>(
        V2PartNavigationDrawerContainerFragmentBinding::inflate
    ) {

    private val viewModel by activityViewModels<NavigationDrawerContainerFragmentViewModel>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    private lateinit var settingsAdapter: UniversalRecyclerViewAdapter<SettingsDrawerItem>

    @Inject
    lateinit var router: ScheduleFragmentContract


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDrawer()


        val fragment = childFragmentManager.findFragmentByTag(TAG)
        if (fragment == null)
            childFragmentManager.beginTransaction().apply {
                replace(
                    com.schedule.schedule.R.id.navigationDrawerContainer,
                    ScheduleFragmentV2V2(),
                    TAG
                )
                commit()
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d("backPressed", "from $TAG")
            if (stateDrawer())
                closeDrawer()
            else
                requireActivity().finish()
        }
    }

    fun manageDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END))
            drawerLayout.closeDrawer(GravityCompat.END)
        else
            drawerLayout.openDrawer(navView)
    }

    private fun stateDrawer() = drawerLayout.isDrawerOpen(GravityCompat.END)
    private fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.END)


    private fun setupDrawer() {
        drawerLayout = binding.navigationDrawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            R.string.openDrawer,
            R.string.closeDrawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val headerBinding =
            V2DrawerLayoutHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        settingsAdapter = UniversalRecyclerViewAdapter(
            delegates = listOf(
                TitleDelegate {},
                PressItemDelegate {}
            ),
            diffUtilCallback = SettingsDrawerItem.SettingsDrawerItemDiffUtil()
        )
        headerBinding.settingsRecyclerView.adapter = settingsAdapter
        settingsAdapter.items = createSettingsList()

        navView = binding.navigationView

//        navView.setNavigationItemSelectedListener {
//            when (it.itemId) {
//                R.id.settings_option -> router.navigateToSettingsScreen()
//                R.id.brs_option -> launchUrl("https://grade.sfedu.ru/")
//                R.id.lms_option -> launchUrl("https://lms.sfedu.ru/my/")
//                R.id.ictis_option -> launchUrl("https://ictis.sfedu.ru/")
//                R.id.official_option -> launchUrl("https://ictis.sfedu.ru/schedule/")
//                R.id.ictisru_option -> launchUrl("https://ictis.ru/")
//            }
//            true
//        }
    }

    private fun launchUrl(url: String) {
        val state = viewModel.getState()
        if (!state) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else viewModel.launchWebView(url).also { closeDrawer() }


    }

    private fun createSettingsList(): List<SettingsDrawerItem> = listOf(
        SettingsDrawerItem.Title("Навигация"),
        SettingsDrawerItem.PressItem("Настройки", R.drawable.baseline_settings_24) {router.navigateToSettingsScreen()},

        SettingsDrawerItem.Title("Web-сервисы ИКТИБ"),
        SettingsDrawerItem.PressItem("БРС", R.drawable.grade) {launchUrl("https://grade.sfedu.ru/")},
        SettingsDrawerItem.PressItem("ЛМС", R.drawable.moodle) {launchUrl("https://lms.sfedu.ru/my/")},
        SettingsDrawerItem.PressItem("Сайт ИКТИБа", R.drawable.online) {launchUrl("https://ictis.sfedu.ru/")},

        SettingsDrawerItem.Title("Web-расписания"),
        SettingsDrawerItem.PressItem("Официальное", R.drawable.schedule) {launchUrl("https://ictis.sfedu.ru/schedule/")},
        SettingsDrawerItem.PressItem("ictis.ru", R.drawable.schedule) {launchUrl("https://ictis.ru/")},
        SettingsDrawerItem.PressItem("ictis.netlify.app", R.drawable.schedule) {launchUrl("https://ictis.netlify.app/")},
        SettingsDrawerItem.PressItem("neyt6.schedule", R.drawable.schedule) {launchUrl("https://neyt6.github.io/schedule/")},
    )

    companion object {
        private const val TAG = "NavigationDrawerContainerFragment"
    }
}