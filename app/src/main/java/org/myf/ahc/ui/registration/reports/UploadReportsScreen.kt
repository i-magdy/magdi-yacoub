package org.myf.ahc.ui.registration.reports

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import org.myf.ahc.R
import org.myf.ahc.adapters.ReportsAdapter
import org.myf.ahc.databinding.ScreenUploadReportsBinding
import org.myf.ahc.util.FileTypesUtil
import org.myf.ahc.util.FilesSizeUtil.REPORTS_SIZE
import org.myf.ahc.util.FilesSizeUtil.calculateSizePercentage
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

@AndroidEntryPoint
class UploadReportsScreen : Fragment(

), ReportsAdapter.ReportAdapterListener {

    private var _binding: ScreenUploadReportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private lateinit var pickFileIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickFileIntent: Intent
    private val viewModel by activityViewModels<ReportsViewModel>()
    private val coroutine = CoroutineScope(Dispatchers.Default)
    private var size = 0L
    private val adapter = ReportsAdapter(this)
    private val deleteDialog = DeleteReportDialog()
    private val editDialog = EditReportDialog()

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
        viewModel.getReportsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenUploadReportsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val behavior = BottomSheetBehavior.from(
            view.findViewById(R.id.pick_up_chooser_bottom_sheet)
        )
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.reportsRv.apply {
            adapter = this@UploadReportsScreen.adapter
            layoutManager = LinearLayoutManager(context)
        }
        binding.uploadButton.setOnClickListener {
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
        binding.reviewButton.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_navigate_from_upload_to_submit)
        }
    }


    private fun updateUi(ui: ReportsUiState) {
        if (ui.pickFile) {
            pickFileIntentLauncher.launch(pickFileIntent)
        }
        if (ui.pickImage) {
            pickImageIntentLauncher.launch(pickImageIntent)
        }
        size = ui.size
        binding.uploadButton.isEnabled = size < REPORTS_SIZE && !ui.isUploading
        binding.reportsSizeProgress.progress = ui.size.toInt()
        binding.reportsLoadingProgress.visibility = if (ui.isLoading) View.VISIBLE else View.GONE
        binding.reportsMessageTv.visibility = if (ui.isEmpty) View.VISIBLE else View.GONE
        adapter.setDocuments(ui.list.sortedBy { it.name })
        binding.reportsSizePercentageTv.text = calculateSizePercentage(ui.size.toDouble())
        if (ui.progress == 0) {
            binding.reportsProgress.visibility = View.GONE
            binding.reportsFileInProgressTv.visibility = View.GONE
        } else {
            binding.reportsProgress.visibility = View.VISIBLE
            binding.reportsFileInProgressTv.visibility = View.VISIBLE
            binding.reportsFileInProgressTv.text = ui.fileName
            binding.reportsProgress.progress = ui.progress
        }
        if (ui.deleteFile != null && !deleteDialog.isAdded) {
            if (editDialog.isAdded){ editDialog.dismiss() }
            deleteDialog.show(parentFragmentManager, "tag_name")
        }
        if (ui.deleteFile == null && deleteDialog.isAdded) {
            deleteDialog.dismiss()
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
                    try {
                        uri?.let {
                            openImage(it)
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
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
                    try {
                        uri?.let {
                            openDocument(it)
                        }
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    @Throws(FileNotFoundException::class)
    fun openDocument(uri: Uri) = coroutine.launch {
        var name = ""
        val type: String? = requireActivity().contentResolver.getType(uri)
        requireActivity().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val nameIndex =
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
        val inputStream = requireActivity().contentResolver.openInputStream(uri) ?: return@launch
        withContext(Dispatchers.IO) {
            val fileSize = inputStream.available()
            Log.d(
                "File_$type",
                "name: $name\n" +
                        "Size: $fileSize Bytes"
            )
            if (type == FileTypesUtil.PDF) {
                if (viewModel.isFileExist(name)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            getString(R.string.file_exist_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (fileSize < REPORTS_SIZE && (fileSize + size) < REPORTS_SIZE) {
                        inputStream.readBytes().let {
                            viewModel.uploadFile(
                                data = it,
                                name = name
                            )
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                getString(R.string.file_size_message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            if (type == FileTypesUtil.MICROSOFT_WORD) {
                if (viewModel.isFileExist(name)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            getString(R.string.file_exist_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (fileSize < REPORTS_SIZE && (fileSize + size) < REPORTS_SIZE) {
                        inputStream.readBytes().let {
                            viewModel.uploadFile(
                                data = it,
                                name = name
                            )
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                getString(R.string.file_size_message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            inputStream.close()
        }
    }

    @Throws(FileNotFoundException::class)
    fun openImage(uri: Uri) = coroutine.launch {
        var name = ""
        val type = requireActivity().contentResolver.getType(uri)
        requireActivity().contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(
                OpenableColumns.DISPLAY_NAME
            )
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }

        }
        val inputStream = requireActivity().contentResolver.openInputStream(uri) ?: return@launch
        withContext(Dispatchers.IO) {
            val fileSize = inputStream.available()
            Log.d(
                "IMAGE_$name",
                "$type , size: $fileSize Bytes"
            )
            if (type == FileTypesUtil.JPG || type == FileTypesUtil.PNG) {
                if (viewModel.isFileExist(name)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            getString(R.string.file_exist_message),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    if (fileSize < REPORTS_SIZE && (fileSize + size) < REPORTS_SIZE) {
                        if (fileSize > 3_000_000) {
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            val byteStream = ByteArrayOutputStream()
                            bitmap.compress(
                                if (type == FileTypesUtil.JPG) Bitmap.CompressFormat.JPEG
                                else Bitmap.CompressFormat.PNG, 60, byteStream
                            )
                            viewModel.uploadFile(
                                data = byteStream.toByteArray(),
                                name = name
                            )
                        } else {
                            inputStream.readBytes().let {
                                viewModel.uploadFile(
                                    data = it,
                                    name = name
                                )
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                getString(R.string.file_size_message),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            inputStream.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::pickImageIntentLauncher.isInitialized) {
            pickImageIntentLauncher.unregister()
        }
        if (this::pickFileIntentLauncher.isInitialized) {
            pickFileIntentLauncher.unregister()
        }
        coroutine.cancel()
        _binding = null
    }

    override fun onEditFile(path: String) {
        if (!editDialog.isAdded){
            viewModel.getReportByPath(path)
            editDialog.show(parentFragmentManager,"edit_report_dialog")
        }
    }

}