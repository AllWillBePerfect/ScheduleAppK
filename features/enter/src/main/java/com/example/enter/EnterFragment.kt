package com.example.enter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.fragment.app.Fragment
import com.example.enter.adapter.GroupChooseAdapter
import com.example.enter.adapter.GroupItem
import com.example.enter.databinding.FragmentEnterBinding
import com.google.android.material.appbar.MaterialToolbar
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


class EnterFragment : Fragment() {

    private var _binding: FragmentEnterBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: GroupChooseAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupChooseAdapter()
        binding.chooseCardRecyclerView.adapter = adapter
        adapter.submitList(createList())

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.title = "Enter"
        toolbar?.setDisplayHomeAsUpEnabled(false)

        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            if (isOpen) {
//                ExpandCollapse.expand(binding.chooseCardCardView, 0)
                /*val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
                )
                binding.ll.setLayoutParams(param)*/
                expand()
                /*val recyclerViewParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                binding.chooseCardRecyclerView.setLayoutParams(FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    recyclerViewParams.height
                ))*/
            } else {
//                ExpandCollapse.collapse(binding.chooseCardCardView)

                /*val param = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    0.0f
                )
                binding.ll.setLayoutParams(param)*/

                /*binding.chooseCardRecyclerView.setLayoutParams(FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    0
                ))*/
                collapse()
            }
        }
    }

    private fun createList(): MutableList<GroupItem> {
        return mutableListOf(
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
            GroupItem("", ""),
        )
    }

    private fun expand() {

        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(
                (binding.ll.parent as View).width,
                View.MeasureSpec.EXACTLY
            )
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(
                (binding.ll.parent as View).height,
                View.MeasureSpec.AT_MOST
            )
        binding.ll.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight: Int = binding.ll.measuredHeight;

        /*val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
        )
        binding.ll.setLayoutParams(param)*/
        val a = object : Animation() {
            val lp = (binding.ll.layoutParams as LinearLayout.LayoutParams)

            //            val targetHeight = param.height
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                /*binding.ll.layoutParams.height = if (interpolatedTime == 1f) targetHeight
                else (targetHeight * interpolatedTime).toInt()*/
                lp.weight = 1F * interpolatedTime
                binding.ll.layoutParams = lp
                binding.ll.requestLayout()
            }

            /*override fun willChangeBounds(): Boolean {
                return true
            }*/
        }
        a.setDuration(700)
        val path = PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
        binding.ll.startAnimation(a)
    }

    private fun collapse() {
        val initialHeight = binding.ll.measuredHeight
        val a: Animation = object : Animation() {
            val lp = (binding.ll.layoutParams as LinearLayout.LayoutParams)
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                /*if (interpolatedTime == 1f) {
                    binding.ll.layoutParams.height = 0
                } else {
                    binding.ll.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    binding.ll.requestLayout()
                }*/
                lp.weight = 1F - (1F * interpolatedTime)
                binding.ll.layoutParams = lp
                binding.ll.requestLayout()
            }

            /*override fun willChangeBounds(): Boolean {
                return true
            }*/
        }

        a.setDuration(700)
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
        binding.ll.startAnimation(a)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}