package org.myf.demo.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.databinding.ScreenWelcomeBinding
import org.myf.demo.ui.main.MainActivity

@AndroidEntryPoint
class WelcomeScreen: Fragment(){

    private lateinit var binding: ScreenWelcomeBinding
    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() { requireActivity().finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.navigationBarColor = SurfaceColors.SURFACE_0.getColor(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ScreenWelcomeBinding.inflate(layoutInflater,container,false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,onBackPressedCallback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<OnBoardingViewModel>()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state
                    .map { it == 1 }
                    .distinctUntilChanged()
                    .collect {
                        if (it){
                            val toMainActivity = Intent(requireActivity(),
                                MainActivity::class.java)
                            startActivity(toMainActivity)
                            requireActivity().finish()
                        }
                        viewModel.state.cancellable()
                    }
            }
        }
    }
}