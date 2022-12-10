package org.myf.ahc.ui.registration.verify

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.R

@AndroidEntryPoint
class VerifySuccessBottomSheet : BottomSheetDialogFragment(
    R.layout.bottom_sheet_verify_success
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottom_dialog_view)
        isCancelable = false
        val behavior = (dialog!! as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = false
        behavior.isDraggable = false
        view.findViewById<MaterialButton>(R.id.next_to_upload_button).setOnClickListener {
            parentFragment?.findNavController()?.navigate(R.id.action_navigate_to_upload_reports)
            dismissNow()
        }
    }
}