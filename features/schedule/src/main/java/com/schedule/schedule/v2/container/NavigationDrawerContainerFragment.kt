package com.schedule.schedule.v2.container

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.schedule.schedule.databinding.V2PartNavigationDrawerContainerFragmentBinding
import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.schedule.v2.ScheduleFragmentV2V2
import com.schedule.values.R
import com.schedule.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NavigationDrawerContainerFragment :
    BaseFragment<V2PartNavigationDrawerContainerFragmentBinding>(
        V2PartNavigationDrawerContainerFragmentBinding::inflate
    ) {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    @Inject
    lateinit var router: ScheduleFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDrawer()
//        drawerLayout.openDrawer(navView)
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

    fun stateDrawer() = drawerLayout.isDrawerOpen(GravityCompat.END)
    fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.END)


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

        navView = binding.navigationView

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.settings_option -> router.navigateToSettingsScreen()
                R.id.brs_option -> launchUrl("https://grade.sfedu.ru/")
                R.id.lms_option -> launchUrl("https://lms.sfedu.ru/my/")
                R.id.ictis_option -> launchUrl("https://ictis.sfedu.ru/")
                R.id.ictisru_option -> launchUrl("https://ictis.ru/")
                R.id.official_option -> launchUrl("https://ictis.sfedu.ru/")
            }
            true
        }
    }

    fun launchUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "NavigationDrawerContainerFragment"
    }
}