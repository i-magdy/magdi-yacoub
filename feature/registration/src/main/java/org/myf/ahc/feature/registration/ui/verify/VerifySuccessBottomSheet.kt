package org.myf.ahc.feature.registration.ui.verify

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.myf.ahc.core.datastore.PatientDataRepo
import org.myf.ahc.feature.registration.R

import javax.inject.Inject

@AndroidEntryPoint
class VerifySuccessBottomSheet : BottomSheetDialogFragment(
    R.layout.bottom_sheet_verify_success
) {

    @Inject
    lateinit var patientRepo: PatientDataRepo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bottom_dialog_view)
        isCancelable = false
        val behavior = (dialog!! as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = false
        behavior.isDraggable = false
        val secondaryPhoneEt = view.findViewById<TextInputEditText>(R.id.other_phone_et)
        view.findViewById<MaterialButton>(R.id.next_to_upload_button).setOnClickListener {
            lifecycleScope.launch{
                patientRepo.updateSecondaryPhone(
                    secondaryPhone = secondaryPhoneEt.editableText.toString()
                )
                delay(200)
                parentFragment?.findNavController()?.navigate(R.id.action_navigate_to_upload_reports)
                dismissNow()
            }
        }
        lifecycleScope.launch {
            patientRepo.getPatientMessage().collect{
                secondaryPhoneEt.setText(it.secondaryPhone)
            }
        }
    }
}