package org.myf.ahc.feature.registration.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.feature.registration.R
import org.myf.ahc.feature.registration.databinding.ScreenMainRegistrationBinding

@AndroidEntryPoint
class MainRegistrationScreen : Fragment() {

    private var _binding: ScreenMainRegistrationBinding? = null
    private val binding: ScreenMainRegistrationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenMainRegistrationBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<MainViewModel>()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.registerButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigate_to_registration)
        }
        binding.logInButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigate_to_log_in)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}