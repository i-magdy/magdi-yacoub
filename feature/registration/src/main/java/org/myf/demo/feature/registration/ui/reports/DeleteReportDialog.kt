package org.myf.demo.feature.registration.ui.reports

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.myf.demo.feature.registration.databinding.DialogDeleteReportBinding
import org.myf.demo.ui.R as uiResource

class DeleteReportDialog : DialogFragment() {

    private var _binding: DialogDeleteReportBinding? = null
    private val binding get() = _binding!!
    val viewModel by viewModels<ReportsViewModel>({requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setBackgroundDrawableResource(uiResource.drawable.dialog_view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogActionClose.setOnClickListener { dismiss() }
        binding.actionDialogButton.setOnClickListener { viewModel.deleteFile() }
        binding.actionDialogCheckbox.setOnCheckedChangeListener { _, b ->
                binding.actionDialogButton.isEnabled = b
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.uiState.collect{
                    if (it.deleteFile != null) {
                        binding.actionDialogTitle.text = it.deleteFile?.name
                    }else{
                        dismiss()
                    }
                }
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDeleteReportBinding.inflate(layoutInflater)
        return AlertDialog
            .Builder(requireActivity())
            .setView(binding.root)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.removeDeleteFileObserver()
        binding.actionDialogCheckbox.isChecked = false
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val TAG = "Delete Document message dialog"
    }
}