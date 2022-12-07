package org.myf.ahc.ui.registration.reports

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.myf.ahc.R

class PickupChooserDialog : BottomSheetDialogFragment(
    R.layout.dialog_pick_up_chooser
) {

    private val viewModel by activityViewModels<ReportsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = (dialog!! as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = true
        behavior.isDraggable = true
        view.findViewById<MaterialCardView>(R.id.pick_image_cv).setOnClickListener {
            lifecycleScope.launch {
                viewModel.openImage()
                delay(200)
                dismiss()
            }
        }
        view.findViewById<MaterialCardView>(R.id.pick_file_cv).setOnClickListener {
            lifecycleScope.launch {
                viewModel.openFiles()
                delay(200)
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.clearOpenFiles()
    }
}