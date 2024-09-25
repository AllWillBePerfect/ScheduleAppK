package com.schedule.schedule.v2

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.schedule.schedule.databinding.V2FragmentScheduleBinding
import com.schedule.schedule.v2.adapter.viewpager.RecyclerViewDayCurrentDelegate
import com.schedule.schedule.v2.adapter.viewpager.RecyclerViewDayDelegate
import com.schedule.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedule.schedule.v2.container.NavigationDrawerContainerFragment
import com.schedule.schedule.v2.search.SearchFragment
import com.schedule.utils.Result
import com.schedule.utils.sources.SingleEvent
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragmentV2 :
    BaseFragment<V2FragmentScheduleBinding>(V2FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2>()
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var adapter: UniversalRecyclerViewAdapter<ViewPagerItem>

//    @Inject
//    lateinit var router: ScheduleFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupAppbar(binding.toolbar.toolbar, "Расписание")

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        val toolbar = getToolbar()
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setInAnimation(
            requireContext(),
            com.schedule.values.R.anim.slide_in_up
        )
        binding.toolbar.textSwitcher.setOutAnimation(
            requireContext(),
            com.schedule.values.R.anim.slide_out_down
        )

//        for (i in 1..20) {
//            binding.weeksTabLayout.addTab(
//                binding.weeksTabLayout.newTab()
//                    .setText(i.toString() + " Нед.")
//            )
//        }

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(RecyclerViewDayDelegate(), RecyclerViewDayCurrentDelegate {
                viewModel.initializeSchedule()
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

        binding.toolbar.textSwitcher.setOnClickListener(::showKeyboardClickListener)

        val fragment = childFragmentManager.findFragmentByTag(TAG)
        if (fragment == null)
            childFragmentManager.beginTransaction().apply {
                replace(com.schedule.schedule.R.id.v2_inner_fragment, SearchFragment(), TAG)
                commit()
            }

        binding.toolbar.menuIcon.visibility = View.VISIBLE
        binding.toolbar.menuIcon.setOnClickListener {
            val parentFragment = parentFragment as NavigationDrawerContainerFragment?
            parentFragment?.manageDrawer()
        }

        viewModel.testLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Result.Success -> {
                    adapter.items = event.data
                    binding.viewPager.post {
                        val index =
                            event.data.indexOfFirst { item -> item is ViewPagerItem.RecyclerViewCurrentDay }
                        if (index != -1)
                            binding.viewPager.setCurrentItem(index, true)
                    }
                    binding.toolbar.textSwitcher.setText(viewModel.getTitle())

                }

                is Result.Error -> {
                    binding.toolbar.textSwitcher.setText(viewModel.getTitle())
                    Toast.makeText(requireContext(), event.exception.message, Toast.LENGTH_SHORT)
                        .show()
                }

                Result.Loading -> {
                    binding.toolbar.textSwitcher.setText("Загрузка...")
                }

            }
        }

        viewModel.weeksLiveData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ScheduleViewModelV2.ResultWeeksConfig.Success -> {
                    binding.weeksTabLayout.clearOnTabSelectedListeners()
                    binding.weeksTabLayout.post {
                        if (!event.data.isRepeating)
                            for (i in 1..event.data.weeks.size) {
                                binding.weeksTabLayout.addTab(
                                    binding.weeksTabLayout.newTab()
                                        .setText("$i Нед.")
                                )
                            }
                    }
                    handler.post {
                        binding.weeksTabLayout.post {
                            binding.weeksTabLayout.getTabAt(event.data.currentWeek - 1)
                                ?.select()
                            binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener {
                                viewModel.fetchByWeek((it + 1).toString())
                            })
                            viewModel.setBlockingTabsClicks(true)
                            binding.weeksTabLayout.visibility = View.VISIBLE

                        }

                    }


                }

                is ScheduleViewModelV2.ResultWeeksConfig.Error -> {
                    viewModel.setBlockingTabsClicks(true)
                    binding.weeksTabLayout.visibility = View.VISIBLE

                }

                is ScheduleViewModelV2.ResultWeeksConfig.ErrorFetchWeek -> {
                    binding.weeksTabLayout.clearOnTabSelectedListeners()
                    binding.weeksTabLayout.post {
                        if (!event.oldValue.isRepeating)
                            for (i in 1..event.oldValue.weeks.size) {
                                binding.weeksTabLayout.addTab(
                                    binding.weeksTabLayout.newTab()
                                        .setText("$i Нед.")
                                )
                            }
                    }
                    handler.post {
                        binding.weeksTabLayout.post {
                            binding.weeksTabLayout.getTabAt(event.oldValue.currentWeek - 1)
                                ?.select()
                            binding.weeksTabLayout.addOnTabSelectedListener(WeekTabListener {
                                viewModel.fetchByWeek((it + 1).toString())
                            })
                            viewModel.setBlockingTabsClicks(true)
                            binding.weeksTabLayout.visibility = View.VISIBLE

                        }

                    }

                }

                ScheduleViewModelV2.ResultWeeksConfig.Loading -> {
                    viewModel.setBlockingTabsClicks(false)
                }

                ScheduleViewModelV2.ResultWeeksConfig.InitLoading -> {binding.weeksTabLayout.visibility = View.INVISIBLE}
            }

        }

        viewModel.initScheduleLiveData.observe(viewLifecycleOwner) {
            it.event?.let {
                viewModel.isBackStackReturn = true
                viewModel.initSchedule()
            }
        }

        viewModel.blockingTabsClicks.observe(viewLifecycleOwner, ::enableTabClicks)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.initializeSchedule()
            binding.swipeRefresh.isRefreshing = false
        }

    }


    private fun enableTabClicks(isEnable: SingleEvent<Boolean>) {
        isEnable.event?.let {
            for (i in 0 until binding.weeksTabLayout.tabCount) {
                binding.weeksTabLayout.getTabAt(i)?.view?.isEnabled = it
            }
        }
    }

    private fun showKeyboardClickListener(view: View) {
        val fragment = childFragmentManager.findFragmentByTag(TAG) as SearchFragment?

        fragment?.let {
            showKeyboardV2(it.getTextInputEditText())
        }
    }

    @Deprecated("Метод не работает в требуемых условиях")
    private fun showKeyboard(view: EditText?) {
        if (view == null) return
        view.requestFocus()
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

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

    private inner class WeekTabListener(
        private val tabSelected: (Int) -> Unit
    ) : OnTabSelectedListener {
//        private var isFirstFetch = true

        override fun onTabSelected(p0: TabLayout.Tab?) {
//            if (!isFirstFetch)
            tabSelected.invoke(p0?.position ?: 0)
//            else
//                isFirstFetch = false
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {

        }

        override fun onTabReselected(p0: TabLayout.Tab?) {
        }
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


    override fun onResume() {
        super.onResume()
        if (!viewModel.isBackStackReturn)
            viewModel.restoreAdapter()
        viewModel.isBackStackReturn = false
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        viewModel.restoreStateAfterPopBackStack()
        super.onDestroyView()

    }


    companion object {
        private const val TAG = "ScheduleFragmentV2"
    }

}