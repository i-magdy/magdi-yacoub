package org.myf.ahc.feature.registration.ui.submit


import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.ahc.feature.registration.R
import org.myf.ahc.feature.registration.databinding.ScreenSubmitBinding


@AndroidEntryPoint
class SubmitScreen : Fragment() {

    private var _binding: ScreenSubmitBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SubmitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenSubmitBinding.inflate(layoutInflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.syncPatientData()
        binding.editNameIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_submit_to_create)
        }
        binding.editReportIv.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigate_from_submit_to_reports)
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                launch {
                    viewModel.patient
                        .map { it.img }
                        .distinctUntilChanged()
                        .collect{
                        if (it.isNotEmpty()) {
                            /*val inputStream =
                                requireActivity().contentResolver.openInputStream(Uri.parse(it))
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.submitPatientIdIv.scaleType = ImageView.ScaleType.CENTER_CROP
                            binding.submitPatientIdIv.setImageBitmap(bitmap)*/
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}