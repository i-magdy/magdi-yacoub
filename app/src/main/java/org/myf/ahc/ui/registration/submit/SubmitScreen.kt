package org.myf.ahc.ui.registration.submit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.databinding.ScreenSubmitBinding

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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}