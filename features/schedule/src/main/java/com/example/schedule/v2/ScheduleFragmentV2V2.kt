package com.example.schedule.v2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.schedule.databinding.V2FragmentScheduleBinding
import com.example.schedule.v2.adapter.viewpager.RecyclerViewDayCurrentDelegate
import com.example.schedule.v2.adapter.viewpager.RecyclerViewDayDelegate
import com.example.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.example.schedule.v2.container.NavigationDrawerContainerFragment
import com.example.schedule.v2.search.SearchFragment
import com.example.views.BaseFragment
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragmentV2V2 :
    BaseFragment<V2FragmentScheduleBinding>(V2FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2V2>()

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var adapter: UniversalRecyclerViewAdapter<ViewPagerItem>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        val toolbar = getToolbar()
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setInAnimation(
            requireContext(),
            com.example.values.R.anim.slide_in_up
        )
        binding.toolbar.textSwitcher.setOutAnimation(
            requireContext(),
            com.example.values.R.anim.slide_out_down
        )

        val fragment = childFragmentManager.findFragmentByTag(TAG)
        if (fragment == null)
            childFragmentManager.beginTransaction().apply {
                replace(com.example.schedule.R.id.v2_inner_fragment, SearchFragment(), TAG)
                commit()
            }

        binding.toolbar.textSwitcher.setOnClickListener(::showKeyboardClickListener)

        binding.toolbar.menuIcon.visibility = View.VISIBLE
        binding.toolbar.menuIcon.setOnClickListener {
            val parentFragment = parentFragment as NavigationDrawerContainerFragment?
            parentFragment?.manageDrawer()
        }


        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(RecyclerViewDayDelegate(), RecyclerViewDayCurrentDelegate {
//                viewModel.initializeSchedule()
            }),
            diffUtilCallback = ViewPagerItem.ViewPagerItemDiffUtil()
        )
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 6

        TabLayoutMediator(binding.daysBottomTabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "ПН"
                1 -> tab.text = "ВТ"
                2 -> tab.text = "СР"
                3 -> tab.text = "ЧТ"
                4 -> tab.text = "ПТ"
                5 -> tab.text = "СБ"
            }
        }.attach()

//        viewModel.initLiveData.observe(viewLifecycleOwner) { event ->
//            event.eventForCheck?.let {
//                binding.weeksTabLayout.visibility = View.INVISIBLE
//                when (it) {
//                    is Result.Success -> {
//                        adapter.items = it.data.scheduleList
//                        binding.viewPager.post {
//                            val index =
//                                it.data.scheduleList.indexOfFirst { item -> item is ViewPagerItem.RecyclerViewCurrentDay }
//                            if (index != -1)
//                                binding.viewPager.setCurrentItem(index, true)
//                        }
//
//                        binding.weeksTabLayout.clearOnTabSelectedListeners()
//                        binding.weeksTabLayout.removeAllTabs()
//                        binding.weeksTabLayout.post {
//                            for (i in 1..it.data.weeks.size)
//                                binding.weeksTabLayout.addTab(
//                                    binding.weeksTabLayout.newTab().setText("$i Нед.")
//                                )
//                        }
//                        handler.post {
//                            binding.weeksTabLayout.post {
//                                binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
//                                    ?.select()
//                                binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener { week ->
//                                    viewModel.fetchByWeek((week + 1).toString())
//                                })
//                                enableTabClicks(true)
//                                binding.weeksTabLayout.visibility = View.VISIBLE
//                            }
//                        }
//                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
//
//                    }
//
//                    is Result.Error -> {
//                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
//                            .show()
//                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
//                        enableTabClicks(true)
//                        binding.weeksTabLayout.visibility = View.VISIBLE
//                    }
//
//                    is Result.Loading -> {
//                        binding.toolbar.textSwitcher.setText("Загрузка...")
//                        enableTabClicks(false)
//                    }
//
//                }
//            }
//        }
//
//        viewModel.weekFetchLiveData.observe(viewLifecycleOwner) { event ->
//            event.event?.let {
//                when (it) {
//                    is Result.Success -> {
//                        adapter.items = it.data.scheduleList
//                        binding.viewPager.post {
//                            val index =
//                                it.data.scheduleList.indexOfFirst { item -> item is ViewPagerItem.RecyclerViewCurrentDay }
//                            if (index != -1)
//                                binding.viewPager.setCurrentItem(index, true)
//                        }
//
//                        handler.post {
//                            binding.weeksTabLayout.post {
//                                binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
//                                    ?.select()
//                                enableTabClicks(true)
//                            }
//                        }
//                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
//                    }
//
//                    is Result.Error -> {
//                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
//                            .show()
//                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
//                        enableTabClicks(true)
//                    }
//
//                    is Result.Loading -> {
//                        binding.toolbar.textSwitcher.setText("Загрузка...")
//                        enableTabClicks(false)
//                    }
//                }
//            }
//        }
//
//        viewModel.backStackRestoreLiveData.observe(viewLifecycleOwner) {
//
//        }
//
//        viewModel.refreshServiceLiveData.observe(viewLifecycleOwner) {
//            it.event?.let { viewModel.init() }
//        }


        viewModel.appBarLoadingLiveData.observe(viewLifecycleOwner) { state ->
            state.event?.let {
                when (it) {
                    is ScheduleViewModelV2V2.AppBarLoadingLiveDataState.Loaded -> {
                        binding.toolbar.textSwitcher.setText(viewModel.getTitle())
                    }

                    is ScheduleViewModelV2V2.AppBarLoadingLiveDataState.Loading -> {
                        binding.toolbar.textSwitcher.setText("Загрузка...")
                    }

                }
            }
        }

        viewModel.tabsLayoutLiveData.observe(viewLifecycleOwner) {state ->
            state.event?.let {
                when (it) {
                    is ScheduleViewModelV2V2.TabsLayoutLiveDataState.SuccessInit -> {
                        binding.weeksTabLayout.clearOnTabSelectedListeners()
                        binding.weeksTabLayout.removeAllTabs()
                        binding.weeksTabLayout.post {
                            for (i in 1..it.data.weeks.size)
                                binding.weeksTabLayout.addTab(
                                    binding.weeksTabLayout.newTab().setText("$i Нед.")
                                )
                        }
                        handler.post {
                            binding.weeksTabLayout.post {
                                binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
                                    ?.select()
                                binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener { week ->
                                    viewModel.fetchByWeekV2((week + 1).toString())
                                })
                                enableTabClicks(true)

                                viewModel.setAppBarLoadingLiveDataLoadedState()
                            }
                        }
                    }
                    is ScheduleViewModelV2V2.TabsLayoutLiveDataState.SuccessWeekFetch -> {
                        binding.weeksTabLayout.clearOnTabSelectedListeners()
                        handler.post {
                            binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
                                ?.select()
                            binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener { week ->
                                viewModel.fetchByWeekV2((week + 1).toString())
                            })
                            enableTabClicks(true)

                            viewModel.setAppBarLoadingLiveDataLoadedState()
                        }
                    }
                    is ScheduleViewModelV2V2.TabsLayoutLiveDataState.Error -> {}
                    is ScheduleViewModelV2V2.TabsLayoutLiveDataState.NotLoadedWeekError -> {
                        binding.weeksTabLayout.clearOnTabSelectedListeners()
                        handler.post {
                            binding.weeksTabLayout.getTabAt(it.oldValue - 1)
                                ?.select()
                            binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener { week ->
                                viewModel.fetchByWeekV2((week + 1).toString())
                            })
                            enableTabClicks(true)

                            viewModel.setAppBarLoadingLiveDataLoadedState()
                        }
                    }
                    ScheduleViewModelV2V2.TabsLayoutLiveDataState.Loading -> {
                        enableTabClicks(false)
                    }

                    ScheduleViewModelV2V2.TabsLayoutLiveDataState.LoadingShimmer -> {
                        enableTabClicks(false)
                    }
                }
            }
        }

        viewModel.adapterLiveData.observe(viewLifecycleOwner) {state ->
            state.event?.let {
                when (it) {
                    is ScheduleViewModelV2V2.AdapterLiveDataState.Success -> {
                        adapter.items = it.data.scheduleList
                        binding.viewPager.post {
                            val index =
                                it.data.scheduleList.indexOfFirst { item -> item is ViewPagerItem.RecyclerViewCurrentDay }
                            if (index != -1)
                                binding.viewPager.setCurrentItem(index, true)
                            binding.viewPager.visibility = View.VISIBLE
                        }
                    }
                    is ScheduleViewModelV2V2.AdapterLiveDataState.Error -> {
                        viewModel.setAppBarLoadingLiveDataLoadedState()
                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
                            .show()
                        binding.viewPager.visibility = View.VISIBLE

                    }
                    ScheduleViewModelV2V2.AdapterLiveDataState.Loading -> {
//                        binding.viewPager.visibility = View.INVISIBLE
                    }

                    ScheduleViewModelV2V2.AdapterLiveDataState.LoadingShimmer -> {

                    }
                }
            }

        }

        viewModel.refreshServiceLiveData.observe(viewLifecycleOwner) {
            it.event?.let { viewModel.initV2() }
        }

        viewModel.restoreAfterPopBackStackServiceLiveData.observe(viewLifecycleOwner) {
            it.event?.let { viewModel.restoreStateAfterPopBackStack() }
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.initV2()
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.restoreStatePopBackStack()

    }

    private fun enableTabClicks(isEnable: Boolean) {
        for (i in 0 until binding.weeksTabLayout.tabCount) {
            binding.weeksTabLayout.getTabAt(i)?.view?.isEnabled = isEnable
        }
    }

    private fun showKeyboardClickListener(view: View) {
        val fragment = childFragmentManager.findFragmentByTag(TAG) as SearchFragment?

        fragment?.let {
            showKeyboardV2(it.getTextInputEditText())
        }
    }

    /**Вторая версия работает в условиях когда edittext находится во viewgroup с шириной 0dp.*/
    private fun showKeyboardV2(view: EditText?) {
        if (view == null) return
        WindowCompat.getInsetsController(
            requireActivity().window,
            view
        ).show(
            WindowInsetsCompat.Type.ime()
        )

    }

    private fun expand() {
        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight =
                TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            val targetHeight: Int = actionBarHeight

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    val calculatedHeight = (targetHeight * interpolatedTime).toInt()

                    if (calculatedHeight == 0)
                        binding.toolbar.toolbar.layoutParams.height = 1
                    else
                        binding.toolbar.toolbar.layoutParams.height =
                            calculatedHeight
                    binding.toolbar.toolbar.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration = 700
            val path =
                PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
            val pathInterpolator = PathInterpolator(path)
            a.interpolator = pathInterpolator
            binding.toolbar.toolbar.startAnimation(a)
        }
    }

    private fun collapse() {

        val initialHeight = binding.toolbar.toolbar.layoutParams.height
//        val initialHeight = 168
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1F)
                    binding.toolbar.toolbar.layoutParams.height = 1
                else
                    binding.toolbar.toolbar.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()

                binding.toolbar.toolbar.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = 700
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
        binding.toolbar.toolbar.startAnimation(a)
    }

    fun hideToolbar() = collapse()
    fun showToolbar() = expand()

    private inner class WeekTabListener(
        private val tabSelected: (Int) -> Unit
    ) : OnTabSelectedListener {

        override fun onTabSelected(p0: TabLayout.Tab?) =
            tabSelected.invoke(p0?.position ?: 0)


        override fun onTabUnselected(p0: TabLayout.Tab?) = Unit


        override fun onTabReselected(p0: TabLayout.Tab?) = Unit
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
//        viewModel.restoreState()
        viewModel.isBackStackReturn = true
//        viewModel.restoreStateAfterPopBackStack()

    }

    override fun onResume() {
        super.onResume()
        if (!viewModel.isBackStackReturn)
            viewModel.onResume()
        viewModel.isBackStackReturn = false
    }

    companion object {
        private const val TAG = "ScheduleFragmentV2V2"
    }

}