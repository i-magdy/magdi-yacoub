package org.myf.ahc.ui.registration.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import org.myf.ahc.R
import org.myf.ahc.databinding.ScreenMainRegistrationBinding

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

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}