package com.schedulev2.schedule.v1

import android.R
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.schedulev2.data.repositories.CurrentLessonRepository
import com.schedulev2.schedule.v1.adapter.ScheduleAdapter
import com.schedulev2.schedule.databinding.DrawerLayoutHeaderBinding
import com.schedulev2.schedule.databinding.FragmentScheduleBinding
import com.schedulev2.utils.ScheduleItem
import com.schedulev2.views.adapter.GroupChooseAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.color.MaterialColors
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private var _headerBinding: DrawerLayoutHeaderBinding? = null
    private val headerBinding get() = _headerBinding!!

    private var handler = Handler(Looper.getMainLooper())


    @Inject
    lateinit var router: ScheduleFragmentContract

    private val viewModel by activityViewModels<ScheduleViewModel>()

    private lateinit var adapterViewPager: ScheduleAdapter
    private lateinit var adapterGroup: GroupChooseAdapter

    lateinit var drawerLayout: DrawerLayout
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var navView: NavigationView

    private var isLoading: Boolean = false
    private lateinit var onErrorTitle: String


    @Inject
    lateinit var currentLessonRepository: CurrentLessonRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        _headerBinding = DrawerLayoutHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onErrorTitle = viewModel.getTitle()
        setupAppBar()
        setupAdapters()
        setupDrawerLayout()
        setupSwipeRefreshLayoutListener()
        setupLiveDates()
        setupTabLayoutMediator()
        setupKeyboardAppearListener()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        val title = viewModel.getTitle()
        toolbar?.title = title
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setText(title)
        binding.toolbar.groupTextInputLayout.visibility = View.GONE

        binding.toolbar.textSwitcher.setInAnimation(
            requireContext(),
            com.schedulev2.values.R.anim.slide_in_up
        )
        binding.toolbar.textSwitcher.setOutAnimation(
            requireContext(),
            com.schedulev2.values.R.anim.slide_out_down
        )

        binding.toolbar.textSwitcher.setOnClickListener {
            WindowCompat.getInsetsController(
                requireActivity().window,
                binding.toolbar.groupTextInputEditText
            ).show(
                WindowInsetsCompat.Type.ime()
            )
            binding.toolbar.groupTextInputEditText.requestFocus()
        }

        binding.submitButton.isEnabled = false
        binding.toolbar.groupTextInputEditText.doAfterTextChanged { text ->
            binding.submitButton.isEnabled = !text.isNullOrEmpty()
            binding.toolbar.groupTextInputEditText.error?.let {
                binding.toolbar.groupTextInputEditText.error = null
            }
            viewModel.editTextSet(text.toString())
        }

        binding.submitButton.setOnClickListener {
            viewModel.initGroup(binding.toolbar.groupTextInputEditText.text.toString())
        }

    }

    private fun setupAdapters() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        @ColorInt var colorPrimary = typedValue.data
        colorPrimary = MaterialColors.getColor(
            requireContext(),
            R.attr.colorPrimary,
            R.attr.colorPrimary
        )
        @ColorInt val textColorOther = MaterialColors.getColor(
            requireContext(),
            R.attr.textColorPrimary,
            R.attr.textColorPrimary
        )
        @ColorInt val textColorCurrent = MaterialColors.getColor(
            requireContext(),
            R.attr.colorBackground,
            R.attr.colorBackground
        )


        //        MaterialColors.getColor(requireContext(), R.attr)

        adapterViewPager = ScheduleAdapter(
            ScheduleAdapter.AttrColors(
                colorPrimary,
                textColorOther,
                textColorCurrent,
            )
        )
        binding.viewPager2.adapter = adapterViewPager
        binding.viewPager2.offscreenPageLimit = 6

        adapterGroup = GroupChooseAdapter {
            viewModel.initGroup(it.groupName)
        }
        binding.chooseCardRecyclerView.adapter = adapterGroup
    }

    private fun setupDrawerLayout() {
        drawerLayout = binding.navigationDrawer
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        toggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            com.schedulev2.values.R.string.openDrawer,
            com.schedulev2.values.R.string.closeDrawer
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView = binding.navigationView

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                com.schedulev2.values.R.id.settings_option -> {
                    router.navigateToSettingsScreen()
                }
            }
            true
        }


    }

    private fun setupSwipeRefreshLayoutListener() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.init()
            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 100)
        }
    }

    private fun setupLiveDates() {
        viewModel.weekViewModel.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.event?.let {
                binding.weekTabLayout.clearOnTabSelectedListeners()
                binding.weekTabLayout.removeAllTabs()

                for (i in it.weeksConfig.weeks) {
                    binding.weekTabLayout.addTab(
                        binding.weekTabLayout.newTab()
                            .setText(it.weeksConfig.weeks[i - 1].toString() + " Нед.")
                    )
                }
                when (it) {
                    is ScheduleViewModel.WeekConfigContainer.FromDB -> {
                        binding.weekTabLayout.getTabAt(it.weeksConfig.week - 1)?.select()
                    }

                    is ScheduleViewModel.WeekConfigContainer.FromNetwork -> {

                        handler.postDelayed({
                            binding.weekTabLayout.getTabAt(it.weeksConfig.week - 1)?.select()

                        }, 100)
                    }
                }
                binding.weekTabLayout.addOnTabSelectedListener(WeekTabListener(tabSelected = { clickedWeek ->
                    viewModel.fetchWeekSchedule(week = it.weeksConfig.weeks[clickedWeek])
                }))
            }

            viewModel.scheduleLiveData.observe(viewLifecycleOwner) { singleEvent ->
                singleEvent.event?.let {
                    adapterViewPager.submitList(it)
                    val currentDayIndex =
                        it.indexOfFirst { item -> item is ScheduleItem.CurrentDay }
                    if (currentDayIndex == -1) return@observe
                    binding.viewPager2.setCurrentItem(currentDayIndex, true)
                }
            }
        }

        viewModel.loadingAppBarLiveData.observe(viewLifecycleOwner) {
            it.event?.let {
                //            if (it) {
//                if (isLoading) return@observe
//                binding.toolbar.textSwitcher.setText("Loading")
//                isLoading = true
//            } else {
//                if (!isLoading) return@observe
//                binding.toolbar.textSwitcher.setText(viewModel.getTitle())
//                isLoading = false
//            }
                when (it) {
                    is ScheduleViewModel.AppBarLoading.Loading -> {
                        if (isLoading) return@observe
                        binding.toolbar.textSwitcher.setText("Loading")
                        isLoading = true
                    }

                    is ScheduleViewModel.AppBarLoading.Loaded -> {
                        if (!isLoading) return@observe
                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
                        headerBinding.weekNumber.text =
                            it.scheduleStatus.week.toString() + " Неделя"
                        headerBinding.statusData.text = it.scheduleStatus.status.toString()
                        headerBinding.fetchData.text =
                            it.scheduleStatus.cacheDate + " " + it.scheduleStatus.cacheTime
                        onErrorTitle = viewModel.getTitle()
                        isLoading = false
                    }

                    is ScheduleViewModel.AppBarLoading.LoadedError -> {
                        if (!isLoading) return@observe
                        binding.toolbar.textSwitcher.setText(onErrorTitle)
                        isLoading = false

                    }
                }
            }
        }

        viewModel.groupsLiveData.observe(viewLifecycleOwner) {
            it.event?.let {
                adapterGroup.submitList(it)
                binding.chooseCardRecyclerView.scrollToPosition(0)
            }
        }

        viewModel.closeGroupSearchView.observe(viewLifecycleOwner) {
            WindowCompat.getInsetsController(
                requireActivity().window,
                binding.toolbar.groupTextInputEditText
            ).hide(
                WindowInsetsCompat.Type.ime()
            )
        }

        viewModel.groupsErrorsLiveData.observe(viewLifecycleOwner) {
            it.event?.let {
                when (it.error) {
                    is NullPointerException -> binding.toolbar.groupTextInputEditText.error =
                        "Такой группы не существует"

                    is UnknownHostException -> Toast.makeText(
                        requireContext(),
                        "Проблема с интернет соединением",
                        Toast.LENGTH_SHORT
                    ).show()

                    is SocketTimeoutException -> Toast.makeText(
                        requireContext(),
                        "Лимит отклика превышен. Повторите запрос",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> Toast.makeText(
                        requireContext(),
                        "${it.error}: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }

        viewModel.getRefreshLiveData().observe(viewLifecycleOwner) {
            it.event?.let {
                viewModel.init()
            }
        }
    }

    private fun setupTabLayoutMediator() {
        viewModel.restoreWeeksTabsState()

        TabLayoutMediator(binding.dayTabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "ПН"
                1 -> tab.text = "ВТ"
                2 -> tab.text = "СР"
                3 -> tab.text = "ЧТ"
                4 -> tab.text = "ПТ"
                5 -> tab.text = "СБ"
            }
        }.attach()
    }

    private fun setupKeyboardAppearListener() {
        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            if (isOpen) {
                requireActivity().removeMenuProvider(myMenuProvider)
                binding.toolbar.textSwitcher.visibility = View.GONE
                binding.toolbar.groupTextInputLayout.visibility = View.VISIBLE
                expand()
            } else {
                requireActivity().addMenuProvider(myMenuProvider)
                binding.toolbar.groupTextInputLayout.visibility = View.GONE
                binding.toolbar.textSwitcher.visibility = View.VISIBLE
                collapse()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().removeMenuProvider(myMenuProvider)
        requireActivity().addMenuProvider(myMenuProvider)

//        if (requireActivity().supportFragmentManager.backStackEntryCount >= 1) {
//            for (i in 0 until requireActivity().supportFragmentManager.backStackEntryCount)
//                requireActivity().supportFragmentManager.popBackStack()
//        }

        if (viewModel.getIsFirstLaunch()) {
            viewModel.changeIsFirstLaunch(false)
            return
        } else {
//            adapter.submitList(createList())
            viewModel.updateList()

        }

    }

    override fun onPause() {
        super.onPause()
        requireActivity().removeMenuProvider(myMenuProvider)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }


    inner class WeekTabListener(
        private val tabSelected: (Int) -> Unit
    ) : OnTabSelectedListener {
        private var isFirstFetch = true

        override fun onTabSelected(p0: TabLayout.Tab?) {
            if (!isFirstFetch)
                tabSelected.invoke(p0?.position ?: 0)
            else
                isFirstFetch = false
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {

        }

        override fun onTabReselected(p0: TabLayout.Tab?) {
        }
    }

    val myMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(com.schedulev2.values.R.menu.app_bar_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                com.schedulev2.values.R.id.settings_IconButton -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.END))
                        drawerLayout.closeDrawer(GravityCompat.END)
                    else
                        drawerLayout.openDrawer(navView)
                    true
                }

                else -> false
            }
        }

    }

    private fun expand() {
        val targetHeight: Int =
            binding.root.height - (binding.toolbar.toolbar.height + binding.submitButton.height + 1)
//        binding.ll.layoutParams.height = 0
        binding.globalLl.layoutParams.height = 0
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//                binding.ll.layoutParams.height = (targetHeight * interpolatedTime).toInt()
//                binding.ll.requestLayout()
                binding.globalLl.layoutParams.height = (targetHeight * interpolatedTime).toInt()
                binding.globalLl.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(700)
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
//        binding.ll.startAnimation(a)
        binding.globalLl.startAnimation(a)

    }

    private fun collapse() {
//        val initialHeight = binding.ll.layoutParams.height
        val initialHeight = binding.globalLl.layoutParams.height
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1F) {
//                    binding.ll.layoutParams.height = 0
                    binding.globalLl.layoutParams.height = 0
                } else {
//                    binding.ll.layoutParams.height =
//                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    binding.globalLl.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                }
                binding.ll.requestLayout()
                binding.globalLl.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.setDuration(700)
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
//        binding.ll.startAnimation(a)
        binding.globalLl.startAnimation(a)
    }

    data class TabsState(
        val tabsList: List<Int>,
        val currentTabIndex: Int
    )
}