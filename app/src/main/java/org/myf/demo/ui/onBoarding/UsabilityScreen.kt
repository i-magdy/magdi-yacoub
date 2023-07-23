package org.myf.demo.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.elevation.SurfaceColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.R
import org.myf.demo.ui.main.MainActivity

@AndroidEntryPoint
class UsabilityScreen: Fragment(
    R.layout.screen_usability
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.navigationBarColor = SurfaceColors.SURFACE_2.getColor(requireContext())
        val viewModel by viewModels<OnBoardingViewModel>()
        view.findViewById<MaterialButton>(R.id.usability_next_button)
            .setOnClickListener {
                findNavController().navigate(R.id.action_navigate_to_tracking_screen)
            }

        view.findViewById<MaterialButton>(R.id.usability_skip_button)
            .setOnClickListener {
                viewModel.updateState(2)
            }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.state
                    .map { it == 2 }
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