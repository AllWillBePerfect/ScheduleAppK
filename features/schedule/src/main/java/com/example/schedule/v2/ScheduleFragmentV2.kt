package com.example.schedule.v2

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.schedule.databinding.V2FragmentScheduleBinding
import com.example.schedule.v1.ScheduleFragmentContract
import com.example.schedule.v2.adapter.recyclerview.model.TimetableItem
import com.example.schedule.v2.adapter.viewpager.RecyclerViewDayCurrentDelegate
import com.example.schedule.v2.adapter.viewpager.RecyclerViewDayDelegate
import com.example.schedule.v2.adapter.viewpager.model.ViewPagerItem
import com.example.schedule.v2.search.SearchFragment
import com.example.views.BaseFragment
import com.example.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleFragmentV2 :
    BaseFragment<V2FragmentScheduleBinding>(V2FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2>()
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var adapter: UniversalRecyclerViewAdapter<ViewPagerItem>

    @Inject
    lateinit var router: ScheduleFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupAppbar(binding.toolbar.toolbar, "Расписание")

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        val toolbar = getToolbar()
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setText("Расписание")
        binding.toolbar.textSwitcher.setText("Loading...")
        binding.toolbar.textSwitcher.setInAnimation(
            requireContext(),
            com.example.values.R.anim.slide_in_up
        )
        binding.toolbar.textSwitcher.setOutAnimation(
            requireContext(),
            com.example.values.R.anim.slide_out_down
        )

        for (i in 1..20) {
            binding.weeksTabLayout.addTab(
                binding.weeksTabLayout.newTab()
                    .setText(i.toString() + " Нед.")
            )
        }

        adapter = UniversalRecyclerViewAdapter(
            delegates = listOf(RecyclerViewDayDelegate(), RecyclerViewDayCurrentDelegate {
                router.navigateToSettingsScreen()
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


        handler.post {
            binding.weeksTabLayout.post {
                binding.weeksTabLayout.getTabAt(19)?.select()
                adapter.items = listOf(
                    ViewPagerItem.RecyclerViewCurrentDay(generateLessonsCurrentList()),
                    ViewPagerItem.RecyclerViewDay(generateLessonsList()),
                    ViewPagerItem.RecyclerViewDay(generateLessonsList()),
                    ViewPagerItem.RecyclerViewDay(generateLessonsList()),
                    ViewPagerItem.RecyclerViewDay(generateLessonsList()),
                    ViewPagerItem.RecyclerViewDay(generateLessonsList()),
                )
                binding.toolbar.textSwitcher.setText("Расписание")
            }
        }


        binding.toolbar.textSwitcher.setOnClickListener(::showKeyboardClickListener)

        childFragmentManager.beginTransaction().apply {
            replace(com.example.schedule.R.id.v2_inner_fragment, SearchFragment(), TAG)
            commit()
        }

    }

    private fun generateLessonsList(): List<TimetableItem> = listOf(
        TimetableItem.Title(
            date = "1 сентября",
            dayOfWeekName = "Понедельник",
            groupName = "КТбо4-2"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
    )

    private fun generateLessonsCurrentList(): List<TimetableItem> = listOf(
        TimetableItem.TitleCurrent(
            date = "1 сентября",
            dayOfWeekName = "Понедельник",
            groupName = "КТбо4-2"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Break(
            time = "00:00-00:00",
            lessonName = "Перерыв, флексим",
            progressValue = 66
        ),
        TimetableItem.LessonCurrent(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1",
            progressValue = 66
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
        TimetableItem.Lesson(
            time = "00:00-00:00",
            lessonName = "лек.Методы оптимизации Липко Ю. Ю. LMS Гладков Л. А. LMS-1"
        ),
    )

    private fun showKeyboardClickListener(view: View) {
        binding.toolbar.textSwitcher.setText("Loading")
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

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    companion object {
        private const val TAG = "ScheduleFragmentV2"
    }

}