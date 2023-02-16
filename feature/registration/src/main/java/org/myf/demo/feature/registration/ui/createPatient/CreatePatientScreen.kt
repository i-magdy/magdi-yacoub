package org.myf.demo.feature.registration.ui.createPatient

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.myf.demo.feature.registration.R
import org.myf.demo.feature.registration.databinding.ScreenCreatePatientBinding
import org.myf.demo.feature.registration.util.ActivityLauncherObserver
import kotlin.math.atan


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
        attachMenu()
        updateUi()
        binding.splitCv.setOnClickListener { observer.pickImage() }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState
                    .shareIn(
                        scope = this,
                        replay = 1,
                        started = SharingStarted.WhileSubscribed()
                    ).map { it.isSuccess }
                    .distinctUntilChanged()
                    .collect {
                        if (it) {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.action_navigate_to_verify)
                            viewModel.removeSuccessObserver()
                        }
                    }
            }
        }
    }

    private fun attachMenu(){
        requireActivity().addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_create,menu)
                val item = menu.findItem(R.id.action_create_patient)
                val menuView = item.setActionView(R.layout.create_menu_action_view)
                val create: MaterialButton = menuView.actionView as MaterialButton
                create.setOnClickListener {
                    viewModel.attemptSavePatient(
                        name = binding.patientNameEt.editableText.toString(),
                        id = binding.nationalIdEt.editableText.toString(),
                        email = binding.patientEmailEt.editableText.toString()
                    )
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun updateUi(){
        binding.patientNameEt.doAfterTextChanged { binding.patientNameIl.error = null }
        binding.nationalIdEt.doAfterTextChanged { binding.nationalIdIl.error = null }
        binding.patientEmailEt.doAfterTextChanged { binding.patientEmailIl.error = null }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onImagePicked(uri: Uri) {
        viewModel.setImageUri(img = uri)
    }
}