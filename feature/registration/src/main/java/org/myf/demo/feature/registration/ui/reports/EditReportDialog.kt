package org.myf.demo.feature.registration.ui.reports

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import org.myf.demo.feature.registration.R

class EditReportDialog : BottomSheetDialogFragment(
    R.layout.dialog_edit_report
) {

    private val viewModel by viewModels<ReportsViewModel>({requireParentFragment()})
    private var path: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(PATH_KEY)?.let {
            path = it
        }
        viewModel.getDocumentByPath(path = path)
        val behavior = (dialog!! as BottomSheetDialog).behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isHideable = true
        behavior.isDraggable = true
        val noteEditText = view.findViewById<EditText>(R.id.edit_doc_note_et)
        view.findViewById<MaterialButton>(R.id.edit_doc_delete_button)
            .setOnClickListener {
                if (path.isNotEmpty()) viewModel.onAttemptToDeleteFile(
                    path = path
                )
                dismiss()
            }
        view.findViewById<MaterialButton>(R.id.edit_doc_add_note_button)
            .setOnClickListener {
                val note: String = noteEditText.editableText.toString()
                if (note.isNotBlank()){
                    viewModel.updateDocumentNote(
                        path = path,
                        note = note
                    )
                    dismiss()
                }
            }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.editDocument.collect{
                    if (it != null){
                        view.findViewById<TextView>(R.id.edit_report_doc_title).text = it.name
                        noteEditText.setText(it.note)
                        path = it.path
                    }
                }
            }
        }

    }

    companion object{
        const val tag = "Edit report dialog tag"
        const val PATH_KEY = "document path key"
    }
}