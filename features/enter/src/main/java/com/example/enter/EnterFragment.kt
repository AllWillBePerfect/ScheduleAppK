package com.example.enter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.PathParser
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.views.adapter.GroupChooseAdapter
import com.example.views.adapter.GroupItem
import com.example.enter.databinding.FragmentEnterBinding
import com.google.android.material.appbar.MaterialToolbar
import com.jakewharton.rxbinding2.view.RxView
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxkotlin.addTo
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class EnterFragment : Fragment() {

    private var _binding: FragmentEnterBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EnterViewModel>()

    private lateinit var adapter: GroupChooseAdapter

    private var isLoading: Boolean = false
    private var handler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var enterFragmentContract: EnterFragmentContract

    private var isAddingMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            isAddingMode = requireArguments().getBoolean(BOOLEAN_TAG, false)
        Log.d("EnterFragment isAddingMode", isAddingMode.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEnterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = GroupChooseAdapter { viewModel.fetchGroup(it.groupName) }
        binding.chooseCardRecyclerView.adapter = adapter
        adapter.submitList(createEmptyList())

        if (!isAddingMode) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
            val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
            toolbar?.title = "Enter"
            toolbar?.setDisplayHomeAsUpEnabled(false)
            toolbar?.setDisplayShowTitleEnabled(false)
            binding.toolbar.textSwitcher.setText("Enter")
        } else {
            (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar.toolbar as MaterialToolbar)
            val toolbar = (requireActivity() as AppCompatActivity).supportActionBar
            toolbar?.setDisplayHomeAsUpEnabled(true)
            toolbar?.setDisplayShowTitleEnabled(false)
            binding.toolbar.textSwitcher.setText("Добавить группу")

        }


        viewModel.groupsLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.chooseCardRecyclerView.scrollToPosition(0)
        }

        viewModel.fetchScheduleLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is EnterViewModel.FetchResult.Success -> {
                    WindowCompat.getInsetsController(
                        requireActivity().window,
                        binding.groupTextInputEditText
                    ).hide(
                        WindowInsetsCompat.Type.ime()
                    )
                    if (!isAddingMode)
                        enterFragmentContract.navigateToScheduleScreen(instanceState = savedInstanceState)
                    else {
                        requireActivity().supportFragmentManager.popBackStack()
                        viewModel.setRefreshLiveData()
                    }
                }

                is EnterViewModel.FetchResult.Error -> {
                    when (it.error) {
                        is NullPointerException -> binding.groupTextInputEditText.error =
                            "Такой группы не существует"

                        is UnknownHostException -> Toast.makeText(
                            requireContext(),
                            "Проблема с интернет соединением",
                            Toast.LENGTH_SHORT
                        ).show()

                        is SocketTimeoutException -> Toast.makeText(
                            requireContext(),
                            "Лимит отклика превышен. Повторите запрос",
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> Toast.makeText(
                            requireContext(),
                            "${it.error}: ${it.message}",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                is EnterViewModel.FetchResult.Loading -> if (it.isLoading) {
                    binding.submitButton.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.submitButton.visibility = View.VISIBLE
                }

            }
        }

        viewModel.loadingAppBarLiveData.observe(viewLifecycleOwner) {
            if (it) {
                if (isLoading) return@observe
                binding.toolbar.textSwitcher.setText("Loading")
                isLoading = true
            } else {
                if (!isLoading) return@observe
                binding.toolbar.textSwitcher.setText("Enter")
                isLoading = false
            }
        }


        binding.submitButton.isEnabled = false
        binding.groupTextInputEditText.doAfterTextChanged { text ->
            binding.submitButton.isEnabled = !text.isNullOrEmpty()
            binding.groupTextInputEditText.error?.let {
                binding.groupTextInputEditText.error = null
            }
            viewModel.editTextSet(text.toString())
        }

        binding.toolbar.textSwitcher.setInAnimation(
            requireContext(),
            com.example.values.R.anim.slide_in_up
        )

        binding.toolbar.textSwitcher.setOutAnimation(
            requireContext(),
            com.example.values.R.anim.slide_out_down
        )

        RxView.clicks(binding.submitButton).subscribe {
            viewModel.fetchGroup(binding.groupTextInputEditText.text.toString())
        }.addTo(viewModel.disposables)


        KeyboardVisibilityEvent.setEventListener(requireActivity(), viewLifecycleOwner) { isOpen ->
            if (isOpen) expand() else collapse()
        }

        startDelayedFocus(350)

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

    private fun createEmptyList(): MutableList<GroupItem> {
        return mutableListOf()
    }

    private fun expand() {
        val targetHeight: Int =
            binding.root.height - (binding.toolbar.toolbar.height + binding.groupTextInputLayout.height + binding.submitButton.height + 1)
        binding.ll.layoutParams.height = 1
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                binding.ll.layoutParams.height = (targetHeight * interpolatedTime).toInt()
                binding.ll.requestLayout()
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
        binding.ll.startAnimation(a)

    }

    private fun collapse() {
        val initialHeight = binding.ll.layoutParams.height
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                binding.ll.layoutParams.height = if (interpolatedTime == 1F)
                    1
                else
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                binding.ll.requestLayout()
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
        binding.ll.startAnimation(a)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val BOOLEAN_TAG = "isAdding"
        fun newInstance(isAdding: Boolean): EnterFragment {
            val args = Bundle()
            args.putBoolean(BOOLEAN_TAG, isAdding)
            val fragment = EnterFragment()
            fragment.arguments = args
            return fragment

        }
    }


}