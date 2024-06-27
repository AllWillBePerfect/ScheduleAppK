package com.example.scheduleappk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.SettingsOptionRepository
import com.example.enter.EnterFragment
import com.example.models.sharpref.AppState
import com.example.rxtest.sharednumber.FlowNumberFragment
import com.example.schedule.ScheduleFragment
import com.example.schedule.v2.ScheduleFragmentV2
import com.example.scheduleappk.databinding.ActivityMainBinding
import com.example.scheduleappk.navigation.ActivityRequired
import com.example.scheduleappk.workmanager.SomeWorkManager
import com.example.utils.nightmode.NightModeUtils.applyNightAfterOnCrete
import com.example.utils.nightmode.NightModeUtils.applyNightBeforeOnCrete
import com.example.utils.nightmode.NightModeUtils.setupNightMode
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var activityRequiredSet: Set<@JvmSuppressWildcards ActivityRequired>

    @Inject
    lateinit var appConfigRepository: AppConfigRepository

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface SettingsOptionRepositoryEntryPoint {
        val settingsOptionRepository: SettingsOptionRepository.Impl
    }

    @Inject
    lateinit var settingsOptionRepository: SettingsOptionRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        val settingsOptionRepository = EntryPointAccessors.fromApplication(this, SettingsOptionRepositoryEntryPoint::class.java).settingsOptionRepository
//        applyNightBeforeOnCrete(settingsOptionRepository.getNightModeInt())
//        applyNightAfterOnCrete(settingsOptionRepository.getNightModeInt())
        setupNightMode(settingsOptionRepository.getNightModeInt())
        super.onCreate(savedInstanceState)
        activityRequiredSet.forEach { it.onCreated(this) }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (appConfigRepository.getAppState() != AppState.UNSELECT)
            launchScheduleScreen()
        else
            launchEnterScreen()


//        launchScheduleScreen()
//        launchRxTestScreen()

        SomeWorkManager.launchPeriodicalWorkRequest(this)
    }

    private fun launchEnterScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, EnterFragment())
            commit()
        }
    }

    private fun launchScheduleScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, ScheduleFragment())
            commit()
        }
    }

    private fun launchScheduleScreenV2() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, ScheduleFragmentV2())
            commit()
        }
    }

    private fun launchRxTestScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, FlowNumberFragment())
            commit()
        }
    }

    override fun onNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onNavigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        activityRequiredSet.forEach { it.onDestroyed() }
    }


}