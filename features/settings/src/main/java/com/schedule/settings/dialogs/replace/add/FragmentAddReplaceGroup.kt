package com.schedule.settings.dialogs.replace.add

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import com.schedule.settings.databinding.V2FragmentAddReplaceGroupBinding
import com.schedule.settings.dialogs.replace.adapter.model.ClickableChooseDayDelegate
import com.schedule.settings.dialogs.replace.adapter.model.DayChooseItem
import com.schedule.utils.Result
import com.schedule.views.BaseFragment
import com.schedule.views.adapter.GroupChooseAdapter
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter
import com.google.android.material.appbar.MaterialToolbar
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

class FragmentAddReplaceGroup :
    BaseFragment<V2FragmentAddReplaceGroupBinding>(V2FragmentAddReplaceGroupBinding::inflate) {

    private val viewModel by activityViewModels<AddReplaceGroupViewModel>()

    private lateinit var rect: Rect
    private lateinit var keyboardListener: Unregistrar

    private var handler = Handler(Looper.getMainLooper())

    private lateinit var daysAdapter: UniversalRecyclerViewAdapter<DayChooseItem>
    private lateinit var adapterGroup: GroupChooseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
        val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
        toolbar?.title = "Группа с ВПК"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.textSwitcher.setText("Группа с ВПК")

        daysAdapter = UniversalRecyclerViewAdapter(
            delegates = listOf(ClickableChooseDayDelegate(viewModel::onItemClick)),
            diffUtilCallback = DayChooseItem.DayChooseItemDiffUtil()
        )

        binding.daysRecyclerView.adapter = daysAdapter

        adapterGroup = GroupChooseAdapter { item ->
            if (binding.groupTextInputLayout.hasFocus()) viewModel.fetchGroup(item.groupName)
            else viewModel.fetchVpk(item.groupName)
        }
        binding.recyclerView.adapter = adapterGroup

        binding.groupTextInputEditText.doAfterTextChanged { text ->
            binding.groupTextInputEditText.error?.let {
                binding.groupTextInputEditText.error = null
            }
            viewModel.setTextToGroupLiveData(text.toString())
            viewModel.setGroupNameCorrectParams(false)
        }

        binding.groupTextInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) viewModel.setAdapterListGroupIfExist()
        }

        binding.vpkTextInputEditText.doAfterTextChanged { text ->
            binding.vpkTextInputEditText.error?.let {
                binding.vpkTextInputEditText.error = null
            }
            viewModel.setTextToVpkLiveData(text.toString())
            viewModel.setVpkCorrectParams(false)

        }

        binding.vpkTextInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                if (hasFocus) viewModel.setAdapterListVpkIfExist()
            }
        }

        binding.button.setOnClickListener {
            if (binding.groupTextInputLayout.hasFocus()) viewModel.fetchGroup(binding.groupTextInputEditText.text.toString())
            else viewModel.fetchVpk(binding.vpkTextInputEditText.text.toString())
        }

        viewModel.daysAdapterListLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { list -> daysAdapter.items = list }
        }

        viewModel.groupLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    viewModel.setAdapterListGroup(it.data)
                    binding.recyclerView.scrollToPosition(0)
                }
                is Result.Loading -> {}
                is Result.Error -> {
                    Log.e("FragmentAddReplaceGroup", "Error: ${it.exception.message}")
                }
            }
        }

        viewModel.vpkLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    viewModel.setAdapterListVpk(it.data)
                    binding.recyclerView.scrollToPosition(0)
                }
                is Result.Loading -> {}
                is Result.Error -> {
                    Log.e("FragmentAddReplaceGroup", "Error: ${it.exception.message}")
                }
            }
        }

        viewModel.groupFetchLiveData.observe(viewLifecycleOwner) {
            it.event?.let {event ->
                when (event) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), event.data.name, Toast.LENGTH_SHORT).show()
                        binding.groupTextInputEditText.setText(event.data.name)
                        viewModel.setGroupNameParams(event.data.name)
                    }
                    is Result.Loading -> {}
                    is Result.Error -> {
                        Log.e("FragmentAddReplaceGroup", "Error: ${event.exception.message}")
                    }
                }
            }
        }

        viewModel.vpkFetchLiveData.observe(viewLifecycleOwner) {
            it.event?.let {event ->
                when (event) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), event.data.name, Toast.LENGTH_SHORT).show()
                        binding.vpkTextInputEditText.setText(event.data.name)
                        viewModel.setVpkParams(event.data.name)
                    }
                    is Result.Loading -> {}
                    is Result.Error -> {
                        Log.e("FragmentAddReplaceGroup", "Error: ${event.exception.message}")
                    }
                }
            }
        }


        viewModel.adapterListLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { list -> when (list) {
                is AddReplaceGroupViewModel.EditTextEvent.ToGroupEditText -> {
                    if (binding.groupTextInputEditText.hasFocus()) adapterGroup.submitList(list.value)
                }
                is AddReplaceGroupViewModel.EditTextEvent.ToVpkEditText -> {
                    if (binding.vpkTextInputLayout.hasFocus()) adapterGroup.submitList(list.value)
                }
            } }
        }

        viewModel.resultConfigLiveData.observe(viewLifecycleOwner) {
            it.eventForCheck?.let { config ->
                if (config.isVpkCorrect && config.isGroupNameCorrect) {
                    viewModel.addReplaceConfig(config.groupName, config.vpkName)
                    requireActivity().supportFragmentManager.popBackStack()
                    return@observe
                }
                if (config.isGroupNameCorrect) {
                    binding.vpkTextInputLayout.requestFocus()
                }
                if (config.isVpkCorrect) {
                    binding.groupTextInputLayout.requestFocus()
                }
            }
        }

        viewModel.setDaysList()

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

        startDelayedFocus(250)

    }

    private fun startDelayedFocus(millis: Long) {
        val runnable = Runnable {
            if (binding.groupTextInputEditText.hasFocus()) return@Runnable
            binding.groupTextInputEditText.requestFocus()
            WindowCompat.getInsetsController(
                requireActivity().window,
                binding.groupTextInputEditText
            ).show(
                WindowInsetsCompat.Type.ime()
            )
        }

        handler.postDelayed(runnable, millis)
    }

    private fun expand() {

        binding.recyclerView.layoutParams.height = 1
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.requestLayout()
        val targetHeight: Int =
            rect.bottom - rect.top - binding.toolbar.toolbar.height - binding.groupTextInputLayout.height - binding.daysRecyclerView.height - binding.button.height
        Log.d(
            "SearchFragment expand",
            "targetHeight: $targetHeight \n rectBottom: ${rect.bottom} \n rectTop: ${rect.top}"
        )

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
//                if (interpolatedTime < 0.96f)
//                    binding.recyclerView.layoutParams.height =
//                        (targetHeight * interpolatedTime).toInt()
//                else {
//                    binding.groupTextInputEditText.requestFocus()
//                    binding.root.getWindowVisibleDisplayFrame(rect)
                binding.recyclerView.layoutParams.height =
                    ((targetHeight) * interpolatedTime).toInt()
//                }
                binding.recyclerView.requestLayout()
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
        binding.recyclerView.startAnimation(a)

    }

    private fun collapse() {
//        val lp = binding.partSearchFragment.layoutParams as ViewGroup.LayoutParams
        val initialHeight = binding.recyclerView.layoutParams.height
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
//                if (interpolatedTime == 0F)
                if (interpolatedTime == 1F) {
                    binding.recyclerView.layoutParams.height = 1
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.recyclerView.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                }
                binding.recyclerView.requestLayout()
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
        binding.recyclerView.startAnimation(a)
    }

    override fun onDetach() {
        super.onDetach()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroyView() {
        setupKeyboardAppearListener(false)
        viewModel.restoreReplaceDialog()
        viewModel.nullViewModel()
        super.onDestroyView()
    }
}