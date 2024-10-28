package com.schedulev2.settings.modal

import android.os.Bundle
import android.view.View
import com.schedulev2.settings.databinding.V2SingleConfigModalBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

@Deprecated("Need to remove")
class SingleOptionConfigModal : BaseBottomSheetDialogFragment<V2SingleConfigModalBinding>(V2SingleConfigModalBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    companion object {
        const val TAG = "SingleOptionConfigModal"
    }
}