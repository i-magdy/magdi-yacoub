package org.myf.ahc.ui.registration.reports

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.myf.ahc.R
import org.myf.ahc.util.FileTypesUtil
import java.io.FileNotFoundException

@AndroidEntryPoint
class UploadReportsScreen : Fragment(
    R.layout.screen_upload_reports
) {

    private lateinit var pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private lateinit var pickFileIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickFileIntent: Intent
    private val viewModel by activityViewModels<ReportsViewModel>()
    private val coroutine = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickImageIntent = Intent(Intent.ACTION_PICK)
            .apply {
                type = "image/*"
            }
        pickFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .apply {
                type = "application/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
        pickImageLauncher()
        pickFileLauncher()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior =
            BottomSheetBehavior.from(view.findViewById(R.id.pick_up_chooser_bottom_sheet))
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        view.findViewById<MaterialButton>(R.id.upload_button)
            .setOnClickListener {
                val chooserDialog = PickupChooserDialog()
                chooserDialog.show(parentFragmentManager, "chooser_dialog")
            }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.uiState.collect { updateUi(it) }
                }
            }
        }
    }


    private fun updateUi(ui: ReportsUiState) {
        if (ui.pickFile) {
            pickFileIntentLauncher.launch(pickFileIntent)
        }
        if (ui.pickImage) {
            pickImageIntentLauncher.launch(pickImageIntent)
        }
    }

    private fun pickImageLauncher() {
        pickImageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val uri = data.data
                    var type: String? = ""
                    var name = ""
                    coroutine.launch {
                        try {
                            val inputStream = uri?.let {
                                type = requireActivity().contentResolver.getType(it)
                                requireActivity().contentResolver.query(it, null, null, null, null)
                                    ?.use { cursor ->
                                        val nameIndex = cursor.getColumnIndex(
                                           OpenableColumns.DISPLAY_NAME
                                        )
                                        if (cursor.moveToFirst()) {
                                            name = cursor.getString(nameIndex)
                                        }
                                    }
                                requireActivity().contentResolver.openInputStream(it)
                            }
                            withContext(Dispatchers.IO) {
                                Log.d(
                                    "IMAGE_$name",
                                    "$type , size: ${
                                        inputStream?.available().toString()
                                    } Bytes"
                                )
                                inputStream?.readBytes()?.let {
                                    viewModel.upload(
                                        data = it,
                                        name = name
                                    )
                                }
                            }
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    private fun pickFileLauncher() {
        pickFileIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val uri = data.data
                    var name = ""
                    coroutine.launch {
                        try {
                            var type: String? = ""
                            val inputStream = uri?.let {
                                type = requireActivity().contentResolver.getType(it)
                                requireActivity().contentResolver.query(it, null, null, null, null)
                                    ?.use { cursor ->
                                        val nameIndex =
                                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                                        if (cursor.moveToFirst()) {
                                            name = cursor.getString(nameIndex)
                                        }
                                    }
                                requireActivity().contentResolver.openInputStream(it)
                            }
                            withContext(Dispatchers.IO) {
                                if (type == FileTypesUtil.PDF) {
                                    Log.d(
                                        "PDF_FILE",
                                        "name: $name\n"+
                                        "Size: ${inputStream?.available()} Bytes"
                                    )
                                    inputStream?.readBytes()?.let {
                                        viewModel.upload(
                                            data = it,
                                            name = name
                                        )
                                    }
                                }
                                if (type == FileTypesUtil.MICROSOFT_WORD) {
                                    Log.d(
                                        "WORD_FILE",
                                        "name: $name\n"+
                                        "Size: ${inputStream?.available()} Bytes"
                                    )
                                    inputStream?.readBytes()?.let {
                                        viewModel.upload(
                                            data = it,
                                            name = name
                                        )
                                    }
                                }
                            }
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::pickImageIntentLauncher.isInitialized) {
            pickImageIntentLauncher.unregister()
        }
    }

}