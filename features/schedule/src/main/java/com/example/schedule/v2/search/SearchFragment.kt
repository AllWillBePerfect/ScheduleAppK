package com.example.schedule.v2.search

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowInsets
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.replace
import com.example.schedule.databinding.V2PartSearchFragmentBinding
import com.example.schedule.v1.ScheduleFragmentContract
import com.example.schedule.v2.ScheduleFragmentV2
import com.example.views.BaseFragment
import com.example.views.adapter.GroupChooseAdapter
import com.example.views.adapter.GroupItem
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<V2PartSearchFragmentBinding>(V2PartSearchFragmentBinding::inflate) {

    private lateinit var adapterGroup: GroupChooseAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var rect: Rect
    private lateinit var keyboardListener: Unregistrar

    @Inject
    lateinit var router: ScheduleFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterGroup = GroupChooseAdapter {
            router.navigateToSettingsScreen()
        }
        binding.chooseCardRecyclerView.adapter = adapterGroup
        adapterGroup.submitList(
            listOf(
                GroupItem("123", "1"),
                GroupItem("qwer", "2"),
                GroupItem("fgh", "3"),
                GroupItem("bmf", "4"),
                GroupItem("xcvf", "5"),
                GroupItem("df6", "6"),
                GroupItem("hjkm", "7"),
                GroupItem("987j", "8"),
                GroupItem("hgt", "9"),
            )
        )
        setupKeyboardAppearListener(true)

    }

    private fun setupKeyboardAppearListener(setup: Boolean) {
        if (setup) {
            keyboardListener =
                KeyboardVisibilityEvent.registerEventListener(requireActivity()) { isOpen ->
                    rect = Rect()
                    binding.root.getWindowVisibleDisplayFrame(rect)
                    if (isOpen) {
                        expand()
                    } else {
                        collapse()
                    }
                }
        } else keyboardListener.unregister()

    }


    private fun expand() {
        binding.partSearchFragment.layoutParams.height = 1
        binding.partSearchFragment.visibility = View.VISIBLE
        binding.partSearchFragment.requestLayout()
        val targetHeight: Int =
            rect.bottom - rect.top
        Log.d(
            "SearchFragment expand",
            "targetHeight: $targetHeight \n rectBottom: ${rect.bottom} \n rectTop: ${rect.top}"
        )
        (parentFragment as ScheduleFragmentV2).hideToolbar()

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                if (interpolatedTime < 0.96f)
                    binding.partSearchFragment.layoutParams.height =
                        (targetHeight * interpolatedTime).toInt()
                else {
                    binding.groupTextInputEditText.requestFocus()
                    binding.root.getWindowVisibleDisplayFrame(rect)
                    binding.partSearchFragment.layoutParams.height =
                        ((rect.bottom - rect.top) * interpolatedTime).toInt()
                }
                binding.partSearchFragment.requestLayout()
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
        binding.partSearchFragment.startAnimation(a)

    }

    private fun collapse() {
//        val lp = binding.partSearchFragment.layoutParams as ViewGroup.LayoutParams
        val initialHeight = binding.partSearchFragment.layoutParams.height
        (parentFragment as ScheduleFragmentV2).showToolbar()
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
//                if (interpolatedTime == 0F)
                if (interpolatedTime == 1F) {
                    binding.partSearchFragment.layoutParams.height = 1
                    binding.partSearchFragment.visibility = View.GONE
                } else {
                    binding.partSearchFragment.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                }
                binding.partSearchFragment.requestLayout()
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
        binding.partSearchFragment.startAnimation(a)
    }

    override fun onDestroyView() {
        handler.removeCallbacksAndMessages(null)
        setupKeyboardAppearListener(false)
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        editTextRequestFocus()
    }

    fun getTextInputEditText(): EditText = binding.groupTextInputEditText
    /** Нужно вызывать этот метот в onResume, чтобы клавиатура смогла отображатся после совершения popbackstack.*/
    private fun editTextRequestFocus() = binding.groupTextInputEditText.requestFocus()


}