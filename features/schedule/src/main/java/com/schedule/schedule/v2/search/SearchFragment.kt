package com.schedule.schedule.v2.search

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.EditText
import android.widget.Toast
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.schedule.schedule.databinding.V2PartSearchFragmentBinding
import com.schedule.schedule.v1.ScheduleFragmentContract
import com.schedule.schedule.v2.ScheduleFragmentV2
import com.schedule.schedule.v2.ScheduleFragmentV2V2
import com.schedule.utils.Result
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.GroupChooseAdapter
import com.schedule.views.adapter.GroupItem
import dagger.hilt.android.AndroidEntryPoint
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<V2PartSearchFragmentBinding>(V2PartSearchFragmentBinding::inflate) {

    private val viewModel by activityViewModels<SearchViewModel>()

    private lateinit var adapterGroup: GroupChooseAdapter
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var rect: Rect
    private lateinit var keyboardListener: Unregistrar

    @Inject
    lateinit var router: ScheduleFragmentContract

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterGroup = GroupChooseAdapter {
            viewModel.fetchGroup(it.groupName)
        }
        binding.chooseCardRecyclerView.adapter = adapterGroup
        setupKeyboardAppearListener(true)

        viewModel.groupLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    adapterGroup.submitList(it.data.map { GroupItem(it.name, it.group) })
                    binding.chooseCardRecyclerView.scrollToPosition(0)
                }

                is Result.Loading -> {}
                is Result.Error -> {Log.d(TAG, it.toString())}

            }
        }

        viewModel.fetchLiveData.observe(viewLifecycleOwner) {
            it.event?.let { event ->
                when (event) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), event.data.name, Toast.LENGTH_SHORT).show()
                        viewModel.setSingleConfig(event.data.name)
                        closeKeyboard()
                        fetchLoading(false)
                    }

                    is Result.Loading -> {
                        fetchLoading(true)
                    }

                    is Result.Error -> {
                        Toast.makeText(requireContext(), "${event.exception.message}", Toast.LENGTH_SHORT)
                            .show()
                        fetchLoading(false)
                    }
                }
            }
        }

        binding.submitButton.isEnabled = false
        binding.groupTextInputEditText.doAfterTextChanged { text ->
            binding.submitButton.isEnabled = !text.isNullOrEmpty()
            binding.groupTextInputEditText.error?.let {
                binding.groupTextInputEditText.error = null
            }
            viewModel.setText(text.toString())
        }

        binding.submitButton.setOnClickListener {
            viewModel.fetchGroup(binding.groupTextInputEditText.text.toString())
        }

    }

    private fun closeKeyboard() {
        WindowCompat.getInsetsController(
            requireActivity().window,
            binding.groupTextInputEditText
        ).hide(
            WindowInsetsCompat.Type.ime()
        )
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
        (parentFragment as? ScheduleFragmentV2)?.hideToolbar()
        (parentFragment as? ScheduleFragmentV2V2)?.hideToolbar()

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
        (parentFragment as? ScheduleFragmentV2)?.showToolbar()
        (parentFragment as? ScheduleFragmentV2V2)?.showToolbar()
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

    private fun fetchLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.submitButton.visibility = View.INVISIBLE
            binding.fetchLoading.visibility = View.VISIBLE
        } else {
            binding.fetchLoading.visibility = View.INVISIBLE
            binding.submitButton.visibility = View.VISIBLE
        }
    }

    /** Нужно вызывать этот метот в onResume, чтобы клавиатура смогла отображатся после совершения popbackstack.*/
    private fun editTextRequestFocus() = binding.groupTextInputEditText.requestFocus()

    companion object {
        private const val TAG = "SearchFragment"
    }

}