package com.schedule.settings.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding

open class BaseDialogFragment<T : ViewBinding>(
    private val inflate: (inflater: LayoutInflater, container: ViewGroup?, attachToParent: Boolean) -> T
) : DialogFragment() {
    private var _dialogBinding: T? = null
    val dialogBinding: T get() = _dialogBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _dialogBinding = inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), com.schedule.settings.R.drawable.dialog_corners_bg))
        return dialogBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _dialogBinding = null
    }
}