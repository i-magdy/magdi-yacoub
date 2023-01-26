package org.myf.ahc.feature.registration.ui.createPatient

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.myf.ahc.feature.registration.R
import org.myf.ahc.feature.registration.databinding.ScreenCreatePatientBinding
import org.myf.ahc.feature.registration.util.ActivityLauncherObserver
import org.myf.ahc.feature.registration.util.CreateLauncherListener



@AndroidEntryPoint
class CreatePatientScreen : Fragment(), CreateLauncherListener {

    private var _binding: ScreenCreatePatientBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CreatePatientViewModel>()
    private lateinit var observer: ActivityLauncherObserver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = ActivityLauncherObserver(requireActivity().activityResultRegistry,this)
        lifecycle.addObserver(observer)
        viewModel.sync()
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.patientNameEt.doAfterTextChanged { viewModel.clearError() }
        binding.nationalIdEt.doAfterTextChanged { viewModel.clearError() }
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
            observer.pickImage()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onImagePicked(uri: Uri) {
        viewModel.setImageUri(img = uri)
    }
}