package org.myf.demo.feature.registration.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.feature.registration.R
import org.myf.demo.feature.registration.databinding.ScreenMainRegistrationBinding

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
        viewModel.sync()
        lifecycleScope.launch {
            viewModel.uiState.map {
                it.isUserExist
            }.collect{
                if (it){
                    Navigation.findNavController(view).navigate(R.id.action_navigate_from_main_to_profile)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}