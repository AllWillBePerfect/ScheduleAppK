package com.schedule.schedule.v1.search

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import androidx.core.graphics.PathParser
import androidx.fragment.app.activityViewModels
import com.schedule.schedule.databinding.FragmentComponentSearchBinding
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.GroupChooseAdapter
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

@AndroidEntryPoint
class SearchComponentFragment :
    BaseFragment<FragmentComponentSearchBinding>(FragmentComponentSearchBinding::inflate) {

    private val viewModel by activityViewModels<SearchComponentViewModel>()

    private lateinit var adapter: GroupChooseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupKeyboardAppearListener()
    }

    private fun setupAdapter() {
        adapter = GroupChooseAdapter {

        }

        binding.chooseCardRecyclerView.adapter = adapter
    }

    private fun setupKeyboardAppearListener() {
        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            if (isOpen) {
//                requireActivity().removeMenuProvider(myMenuProvider)
                expand()
            } else {
//                requireActivity().addMenuProvider(myMenuProvider)
                collapse()
            }
        }
    }

    private fun expand() {
        val targetHeight: Int =
            binding.root.height - (binding.toolbar.height + binding.submitButton.height + 1)
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
}