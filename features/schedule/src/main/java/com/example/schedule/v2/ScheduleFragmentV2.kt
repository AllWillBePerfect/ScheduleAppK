package com.example.schedule.v2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.example.schedule.databinding.V2FragmentScheduleBinding
import com.example.schedule.v2.search.SearchFragment
import com.example.views.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class ScheduleFragmentV2 :
    BaseFragment<V2FragmentScheduleBinding>(V2FragmentScheduleBinding::inflate) {

    private val viewModel by activityViewModels<ScheduleViewModelV2>()
    private val handler = Handler(Looper.getMainLooper())


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupAppbar(binding.toolbar.toolbar, "Расписание")

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar)
        val toolbar = getToolbar()
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setText("Расписание")
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

        binding.weeksTabLayout.post {
            binding.weeksTabLayout.getTabAt(19)?.select()
        }


        binding.toolbar.textSwitcher.setOnClickListener {
            val textSwitcherHeight = binding.toolbar.textSwitcher.layoutParams.height
            val toolbarHeight = binding.toolbar.textSwitcher.layoutParams.height
            Log.d("ScheduleFragmentV2", "textSwitcherHeight: $textSwitcherHeight \n toolbarHeight: $toolbarHeight")
            binding.toolbar.textSwitcher.setText("Loading")

            val fragment = childFragmentManager.findFragmentByTag(TAG) as SearchFragment?

            fragment?.let {
                WindowCompat.getInsetsController(
                    requireActivity().window,
                    it.getTextInputEditText()
                ).show(
                    WindowInsetsCompat.Type.ime()
                )
            }

        }


        childFragmentManager.beginTransaction().apply {
            replace(com.example.schedule.R.id.v2_inner_fragment, SearchFragment(), TAG)
            commit()
        }

    }



    fun getInnerFragmentContainerHeight(): Int = binding.root.height
    fun getToolbarHeight(): Int = binding.toolbar.toolbar.height
    fun hideToolbar() = collapse()
    fun showToolbar() = expand()


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

    companion object {
        private const val TAG = "ScheduleFragmentV2"
    }

}