package com.schedulev2.rxtest.sharednumber

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.schedulev2.rxtest.databinding.FragmentAddBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AddFragment : Fragment() {

    var _binding: FragmentAddBinding? = null
    val binding get() = _binding!!

    val viewModel: NumberViewModel by activityViewModels<NumberViewModel>()

    private val selectedNumbersSubject = PublishSubject.create<Int>()
    val selectedNumbers: Observable<Int>
        get() = selectedNumbersSubject.hide()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribeSelectedNumbers(selectedNumbers)

        binding.complete.setOnClickListener {
            selectedNumbersSubject.onNext(binding.textInputEditText.text.toString().toInt())
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        selectedNumbersSubject.onComplete()

    }

    companion object {

    }
}