package com.example.scheduleappk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceManager
import com.example.clear.ClearFragment
import com.example.data.repositories.AppConfigRepository
import com.example.data.repositories.ClearDataRepository
import com.example.data.repositories.SettingsOptionRepository
import com.example.data.repositories.settings.DynamicColorsRepository
import com.example.data.repositories.v2.schedule.repository.impl.AppConfigRepositoryV2
import com.example.enter.EnterFragment
import com.example.models.sharpref.v1.AppState
import com.example.schedule.v1.ScheduleFragment
import com.example.schedule.v2.container.NavigationDrawerContainerFragment
import com.example.scheduleappk.databinding.ActivityMainBinding
import com.example.scheduleappk.navigation.ActivityRequired
import com.example.scheduleappk.workmanager.SomeWorkManager
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

    @Inject
    lateinit var appConfigRepositoryV2: AppConfigRepositoryV2

    @Inject
    lateinit var clearDataRepository: ClearDataRepository

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface SettingsOptionRepositoryEntryPoint {
        val settingsOptionRepository: SettingsOptionRepository.Impl
        val dynamicColorsRepository: DynamicColorsRepository.Impl
    }

    @Inject
    lateinit var settingsOptionRepository: SettingsOptionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val settingsOptionRepository = EntryPointAccessors.fromApplication(
            this,
            SettingsOptionRepositoryEntryPoint::class.java
        ).settingsOptionRepository
        val dynamicColorsRepository = EntryPointAccessors.fromApplication(
            this,
            SettingsOptionRepositoryEntryPoint::class.java
        ).dynamicColorsRepository

//        applyNightBeforeOnCrete(settingsOptionRepository.getNightModeInt())
//        applyNightAfterOnCrete(settingsOptionRepository.getNightModeInt())
        setupNightMode(settingsOptionRepository.getNightModeInt())
        if (dynamicColorsRepository.getDynamicColorsState())
            setTheme(com.example.values.R.style.Theme_ScheduleAppK_DynamicColors)
        else
            setTheme(com.example.values.R.style.Theme_ScheduleAppK)

        super.onCreate(savedInstanceState)
        activityRequiredSet.forEach { it.onCreated(this) }
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

//        appConfigRepositoryV2.addMultipleGroupAndSetState("ктбо4-2")
//        appConfigRepositoryV2.addMultipleGroupAndSetState("впк 1-8")
//        appConfigRepositoryV2.addMultipleGroupAndSetState("ктбо4-1")
//        appConfigRepositoryV2.addMultipleGroupAndSetState("ктбо4-3")
//        appConfigRepositoryV2.addReplaceGroupAndSetState("КТбо4-5", "ВПК 3-8", listOf(1, 5))

//        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        if (checkClear())
            launchClearScreen()
        else {
            clearDataRepository.saveChanges()
            if (appConfigRepository.getAppState() != AppState.UNSELECT)
                launchScheduleScreenV2()
            else
                launchEnterScreen()
        }
        SomeWorkManager.launchPeriodicalWorkRequest(this)
        
    }

    private fun checkClear(): Boolean {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        val cachedValue: String =
            sharedPreferences.getString("cached_version", versionName)!!
        val firstCheck = cachedValue != versionName
        val secondCheck = getString(R.string.clear_storage_flag).toBoolean()
        Log.d("WHAT??", "$cachedValue --- $versionName")
        return firstCheck && secondCheck
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
//            replace(R.id.container_main, ScheduleFragmentV2())
            replace(R.id.container_main, NavigationDrawerContainerFragment(), TAG)
            commit()
        }
    }

    private fun launchClearScreen() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container_main, ClearFragment())
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

    companion object {
        private const val TAG = "MainActivity"
    }


}