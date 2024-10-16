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
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.schedule.schedule.databinding.CustomTabBinding
import com.schedule.schedule.databinding.V2FragmentScheduleBinding
import com.schedule.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.schedule.schedule.v2.adapter.viewpager.RecyclerViewDayCurrentDelegate
import com.schedule.schedule.v2.adapter.viewpager.RecyclerViewDayDelegate
import com.schedule.schedule.v2.adapter.viewpager.adapter.RecyclerViewDayAdapter
import com.schedule.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.schedule.schedule.v2.container.NavigationDrawerContainerFragment
import com.schedule.schedule.v2.search.SearchFragment
import com.schedule.utils.sources.DateUtils
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class ScheduleFragmentV2V2 :
    BaseFragment<V2FragmentScheduleBinding>(V2FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2V2>()

    private val handler = Handler(Looper.getMainLooper())

    //    private lateinit var adapter: UniversalRecyclerViewAdapter<ViewPagerItem>
    private lateinit var adapter: RecyclerViewDayAdapter

    private var currentWeek by Delegates.notNull<Int>()

    private var baseTextColor by Delegates.notNull<Int>()
    private var primaryTextColor by Delegates.notNull<Int>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setColors()

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

        val fragment = childFragmentManager.findFragmentByTag(TAG)
        if (fragment == null)
            childFragmentManager.beginTransaction().apply {
                replace(com.schedule.schedule.R.id.v2_inner_fragment, SearchFragment(), TAG)
                commit()
            }

        binding.toolbar.textSwitcher.setOnClickListener(::showKeyboardClickListener)

        binding.toolbar.menuIcon.visibility = View.VISIBLE
        binding.toolbar.menuIcon.setOnClickListener {
            val parentFragment = parentFragment as NavigationDrawerContainerFragment?
            parentFragment?.manageDrawer()
        }


//        adapter = UniversalRecyclerViewAdapter(
//            delegates = listOf(RecyclerViewDayDelegate(), RecyclerViewDayCurrentDelegate {
////                viewModel.initializeSchedule()
//            }),
//            diffUtilCallback = ViewPagerItem.ViewPagerItemDiffUtil()
//        )

        adapter = RecyclerViewDayAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 6

        TabLayoutMediator(binding.daysBottomTabLayout, binding.viewPager) { tab, position ->
            val tabBinding = CustomTabBinding.inflate(layoutInflater)
            when (position) {
                0 -> tabBinding.tabText.text = "ПН"
                1 -> tabBinding.tabText.text = "ВТ"
                2 -> tabBinding.tabText.text = "СР"
                3 -> tabBinding.tabText.text = "ЧТ"
                4 -> tabBinding.tabText.text = "ПТ"
                5 -> tabBinding.tabText.text = "СБ"
            }
            if (DateUtils.getCurrentDayOfWeek() - 1 == position)
                tabBinding.tabText.setTextColor(primaryTextColor)

            tab.customView = tabBinding.root
        }.attach()

        manageMultScrollTabLayout()


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

        viewModel.tabsLayoutLiveData.observe(viewLifecycleOwner) { state ->
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
                            currentWeek = it.data.currentWeek
                            for (i in 0 until binding.weeksTabLayout.tabCount) {
                                val tab = binding.weeksTabLayout.getTabAt(i)
                                tab?.let { letTab ->
                                    val tabBinding = CustomTabBinding.inflate(layoutInflater)
                                    tabBinding.tabText.text = letTab.text
                                    if (i == currentWeek - 1) tabBinding.tabText.setTextColor(
                                        primaryTextColor
                                    )
                                    tab.setCustomView(tabBinding.root)
                                }
                            }

                            handler.post {
                                binding.weeksTabLayout.post {
                                    binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
                                        ?.select()
                                    binding.weeksTabLayout.addOnTabSelectedListener(
                                        WeekTabListener(
                                            currentWeek
                                        ) { week ->
                                            viewModel.fetchByWeekV2((week + 1).toString())
                                        })
                                    enableTabClicks(true)
                                    viewModel.setAppBarLoadingLiveDataLoadedState()
                                }
                            }
                        }

                    }

                    is ScheduleViewModelV2V2.TabsLayoutLiveDataState.SuccessWeekFetch -> {
                        binding.weeksTabLayout.clearOnTabSelectedListeners()
                        handler.post {
                            binding.weeksTabLayout.getTabAt(it.data.currentWeek - 1)
                                ?.select()
                            binding.weeksTabLayout.addOnTabSelectedListener(
                                WeekTabListener(
                                    currentWeek
                                ) { week ->
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
                            binding.weeksTabLayout.addOnTabSelectedListener(
                                WeekTabListener(
                                    currentWeek
                                ) { week ->
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

        viewModel.adapterLiveData.observe(viewLifecycleOwner) { state ->
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

                            val groups = it.data.scheduleList[0].let { item ->
                                when (item) {
                                    is ViewPagerItem.RecyclerViewDay -> {
                                        item.lessons.filterIsInstance<TimetableItem.Title>()
                                            .map { it.groupName }
                                    }

                                    is ViewPagerItem.RecyclerViewCurrentDay -> {
                                        item.lessons.filterIsInstance<TimetableItem.TitleCurrent>()
                                            .map { it.groupName }
                                    }
                                }
                            }
                            binding.multipleGroupBottomTabLayout.removeAllTabs()
                            for (i in groups) {
                                binding.multipleGroupBottomTabLayout.addTab(
                                    binding.multipleGroupBottomTabLayout.newTab().setText(i)
                                )
                            }
                            binding.multipleGroupBottomTabLayout.clearOnTabSelectedListeners()
                            binding.multipleGroupBottomTabLayout.addOnTabSelectedListener(
                                GroupScrollTabListener { groupName ->
                                    for (i in 0 until adapter.items.size) {
                                        val viewHolder = adapter.getViewHolder(i)
                                        if (viewHolder is RecyclerViewDayDelegate.RecyclerViewDayViewHolder) {
                                            val items =
                                                (viewHolder.binding.recyclerView.adapter as UniversalRecyclerViewAdapter<TimetableItem>).items

                                            for (j in items.indices) {
                                                val item = items[j]
                                                if (item is TimetableItem.Title) {
                                                    if (item.groupName == groupName) {
                                                        val position = j
                                                        viewHolder.binding.recyclerView.smoothScrollToPositionWithOffset(
                                                            position,
                                                            0
                                                        )
                                                        break
                                                    }
                                                }
                                            }
                                        } else if (viewHolder is RecyclerViewDayCurrentDelegate.RecyclerViewDayCurrentViewHolder) {
                                            for (j in 0 until viewHolder.binding.recyclerView.childCount) {
                                                val items =
                                                    (viewHolder.binding.recyclerView.adapter as UniversalRecyclerViewAdapter<TimetableItem>).items

                                                for (j in items.indices) {
                                                    val item = items[j]
                                                    if (item is TimetableItem.TitleCurrent) {
                                                        if (item.groupName == groupName) {
                                                            val position = j
                                                            viewHolder.binding.recyclerView.smoothScrollToPositionWithOffset(
                                                                position,
                                                                0
                                                            )
                                                            break
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                })
//                            for (i in 0 until adapter.items.size) {
//                                when (val viewHolder = adapter.getViewHolder(i)) {
//                                    is RecyclerViewDayDelegate.RecyclerViewDayViewHolder -> {
//                                        viewHolder.binding.recyclerView.addOnScrollListener(object :
//                                            OnScrollListener() {
//                                            override fun onScrolled(
//                                                recyclerView: RecyclerView,
//                                                dx: Int,
//                                                dy: Int
//                                            ) {
//                                                super.onScrolled(recyclerView, dx, dy)
//
//                                                val firstVisiblePosition =
//                                                    (viewHolder.binding.recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//
//                                            }
//                                        })
//                                    }
//
//                                    is RecyclerViewDayCurrentDelegate.RecyclerViewDayCurrentViewHolder -> {
//
//                                    }
//                                }
//                            }

                        }
                    }

                    is ScheduleViewModelV2V2.AdapterLiveDataState.Error -> {
                        viewModel.setAppBarLoadingLiveDataLoadedState()
//                        Toast.makeText(requireContext(), it.exception.message, Toast.LENGTH_SHORT)
//                            .show()
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

//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                for (i in 0 until adapter.items.size) {
//                    val viewHolder = adapter.getViewHolder(i)
//                    if (viewHolder is RecyclerViewDayDelegate.RecyclerViewDayViewHolder) {
//                        viewHolder.binding.recyclerView.scrollToPosition(4)
//                    } else if (viewHolder is RecyclerViewDayCurrentDelegate.RecyclerViewDayCurrentViewHolder) {
//                        viewHolder.binding.recyclerView.scrollToPosition(4)
//                    }
//                }
//            }
//        })


        viewModel.refreshServiceLiveData.observe(viewLifecycleOwner) {
            it.event?.let {
                viewModel.initV2()
                manageMultScrollTabLayout()
            }
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

    private fun manageMultScrollTabLayout() {
        viewModel.manageViewMultipleScrollTabLayout({
            binding.multipleGroupBottomTabLayout.visibility = View.VISIBLE
        }, {
            binding.multipleGroupBottomTabLayout.visibility = View.GONE
        })
    }

    private fun setColors() {
        val typedValue = TypedValue()
        val theme = requireContext().theme
        val attr = R.attr.colorOnSurfaceVariant
        theme.resolveAttribute(
            attr,
            typedValue,
            true
        )
        baseTextColor = if (typedValue.resourceId != 0)
            ContextCompat.getColor(requireContext(), typedValue.resourceId)
        else typedValue.data

        val typedValue2 = TypedValue()
        val theme2 = requireContext().theme
        val attr2 = R.attr.colorPrimary
        theme2.resolveAttribute(
            attr2,
            typedValue2,
            true
        )
        primaryTextColor = if (typedValue2.resourceId != 0)
            ContextCompat.getColor(requireContext(), typedValue2.resourceId)
        else typedValue2.data
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
        private val currentWeek: Int,
        private val tabSelected: (Int) -> Unit
    ) : OnTabSelectedListener {

        override fun onTabSelected(p0: TabLayout.Tab?) {
            val customView = p0?.customView
            customView?.let {
                val tabBinding = CustomTabBinding.bind(it)
                if (p0.position == currentWeek - 1)
                    tabBinding.tabText.setTextColor(primaryTextColor)
                else
                    tabBinding.tabText.setTextColor(baseTextColor)
            }
            tabSelected.invoke(p0?.position ?: 0)
        }


        override fun onTabUnselected(p0: TabLayout.Tab?) {
            val customView = p0?.customView
            customView?.let {
                val tabBinding = CustomTabBinding.bind(it)
                if (p0.position == currentWeek - 1)
                    tabBinding.tabText.setTextColor(primaryTextColor)
                else
                    tabBinding.tabText.setTextColor(baseTextColor)
            }
        }


        override fun onTabReselected(p0: TabLayout.Tab?) = Unit
    }

    private inner class GroupScrollTabListener(
        private val groupSelected: (String) -> Unit
    ) : OnTabSelectedListener {

        override fun onTabSelected(p0: TabLayout.Tab?) {
            val tabPosition = p0?.position
            tabPosition?.let {
                val groupName = binding.multipleGroupBottomTabLayout.getTabAt(it)?.text
//                Toast.makeText(requireContext(), groupName, Toast.LENGTH_SHORT).show()
                groupName?.let {
                    groupSelected.invoke(it.toString())
                }
            }

        }


        override fun onTabUnselected(p0: TabLayout.Tab?) = Unit


        override fun onTabReselected(p0: TabLayout.Tab?) {
            val tabPosition = p0?.position
            tabPosition?.let {
                val groupName = binding.multipleGroupBottomTabLayout.getTabAt(it)?.text
//                Toast.makeText(requireContext(), groupName, Toast.LENGTH_SHORT).show()
                groupName?.let {
                    groupSelected.invoke(it.toString())
                }
            }
        }
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

    fun scrollToPositionInInnerMostRecyclerView(
        outerPosition: Int,
        innerPosition: Int,
        innerMostPosition: Int
    ) {
        val outerRecyclerView = binding.viewPager
    }

    class CustomSmoothScroller(context: Context, private val offset: Int) :
        LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START // Устанавливаем чтобы элемент появлялся в начале
        }

        override fun calculateDyToMakeVisible(view: View?, snapPreference: Int): Int {
            // Задаем свой оффсет
            return super.calculateDyToMakeVisible(view, snapPreference) - offset
        }


    }

    // Функция для плавной прокрутки с оффсетом
    private fun RecyclerView.smoothScrollToPositionWithOffset(position: Int, offset: Int) {
        val layoutManager = this.layoutManager as? LinearLayoutManager
        layoutManager?.let {
            val smoothScroller: RecyclerView.SmoothScroller =
                CustomSmoothScroller(this.context, offset)
            smoothScroller.targetPosition = position
            it.startSmoothScroll(smoothScroller)
        }
    }

    data class GroupsScrollsPosition(
        val listGroup: List<String>,
        val listPositions: List<Int>
    )

    fun updateGroupsScrollsPositionIndexes(
        listPositions: List<String>,
        updateList: Boolean = false
    ) {

    }

}