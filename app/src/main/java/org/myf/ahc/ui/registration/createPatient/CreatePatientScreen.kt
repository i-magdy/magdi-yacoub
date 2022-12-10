package org.myf.ahc.ui.registration.createPatient

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
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.myf.ahc.R
import org.myf.ahc.databinding.ScreenCreatePatientBinding
import org.myf.ahc.util.CreatePatientUiError
import java.io.FileNotFoundException

@AndroidEntryPoint
class CreatePatientScreen :Fragment(){
    private var _binding: ScreenCreatePatientBinding? = null
    private val binding get() = _binding!!
    private lateinit var  pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private lateinit var imageView: ImageView
    private val viewModel by viewModels<CreatePatientViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        _binding = ScreenCreatePatientBinding.inflate(layoutInflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.patientName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.patientNameEt.doAfterTextChanged {
            viewModel.setPatientName(it?.toString() ?: "")
            binding.patientNameIl.helperText = null
            viewModel.clearError()
        }
        binding.nationalIdEt.doAfterTextChanged {
            viewModel.setNationalId(it?.toString() ?: "")
            binding.nationalIdIl.helperText = null
            viewModel.clearError()
        }
        val nextButton = view.findViewById<MaterialButton>(R.id.next_button)
        nextButton.setOnClickListener {
            viewModel.validateInput();
            Navigation.findNavController(view).navigate(R.id.action_navigate_to_verify)
        }
        view.findViewById<ImageView>(R.id.national_id_image)
            .setOnClickListener {
                pickImageIntentLauncher.launch(pickImageIntent)
            }
        imageView = view.findViewById(R.id.national_id_image)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.uiState.collect{
                        showUiError(it.error)
                    }
                }
            }
        }
    }

    private fun showUiError(error: CreatePatientUiError) = when(error){
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


    private fun pickImageLauncher(){
        pickImageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val uri = data.data
                    try {
                        val inputStream = uri?.let {
                            requireActivity().contentResolver.openInputStream(it)
                        }
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        //TODO update ui here e.g. { binding.imageView.setImageBitmap(bitmap) }
                        imageView.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this::pickImageIntentLauncher.isInitialized){
            pickImageIntentLauncher.unregister()
        }
    }
}