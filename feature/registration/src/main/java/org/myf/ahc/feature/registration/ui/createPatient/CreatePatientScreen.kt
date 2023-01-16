package org.myf.ahc.feature.registration.ui.createPatient

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.myf.ahc.feature.registration.R
import org.myf.ahc.feature.registration.databinding.ScreenCreatePatientBinding
import org.myf.ahc.feature.registration.util.CreatePatientUiError

@AndroidEntryPoint
class CreatePatientScreen : Fragment() {

    private var _binding: ScreenCreatePatientBinding? = null
    private val binding get() = _binding!!
    private lateinit var pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private val viewModel by viewModels<CreatePatientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.sync()
        pickImageIntent = Intent(Intent.ACTION_PICK)
            .apply {
                type = "image/*"
            }
        pickImageLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenCreatePatientBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.patientNameEt.doAfterTextChanged {
            binding.patientNameIl.helperText = null
            viewModel.clearError()
        }
        binding.nationalIdEt.doAfterTextChanged {
            binding.nationalIdIl.helperText = null
            viewModel.clearError()
        }
        binding.nextButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.attemptSavePatient(
                    name = binding.patientNameEt.editableText.toString(),
                    id = binding.nationalIdEt.editableText.toString(),
                    email = binding.patientEmailEt.editableText.toString()
                )
                delay(200)
                Navigation.findNavController(view).navigate(R.id.action_navigate_to_verify)
            }
        }
        binding.splitCv.setOnClickListener {
            pickImageIntentLauncher.launch(pickImageIntent)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.uiState.collect { updateUi(it) }
                }
            }
        }
    }

    private fun showUiError(
        error: CreatePatientUiError
    ) = when (error) {
        CreatePatientUiError.INVALID_PATIENT_NAME -> {
            binding.patientNameIl.error = getString(R.string.invalid_patient_name)
        }
        CreatePatientUiError.INVALID_NATIONAL_ID -> {
            binding.nationalIdEt.error = getString(R.string.invalid_national_id)
        }
        //iamge errors.....?
        CreatePatientUiError.NONE -> {
            binding.patientNameIl.error = null
            binding.patientNameIl.error = null
        }
    }


    private fun updateUi(ui: CreatePatientUiState) {
        showUiError(ui.error)
        if (ui.img.toString().isNotEmpty()) {
            try {
                val inputStream = requireActivity().contentResolver.openInputStream(ui.img)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.nationalIdImage.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.nationalIdImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (ui.email.isNotEmpty()){
            binding.patientEmailEt.setText(ui.email)
        }
        if (ui.nationalId.isNotEmpty()){
            binding.nationalIdEt.setText(ui.nationalId)
        }
        if (ui.patientName.isNotEmpty()){
            binding.patientNameEt.setText(ui.patientName)
        }
    }

    private fun pickImageLauncher() {
        pickImageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val uri = data.data
                    uri?.let {
                        viewModel.setImageUri(it)
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if (this::pickImageIntentLauncher.isInitialized) {
            pickImageIntentLauncher.unregister()
        }
    }
}