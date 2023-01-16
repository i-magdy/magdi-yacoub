package org.myf.ahc.ui.onBoarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import org.myf.ahc.R
import org.myf.ahc.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchScreen : Fragment(
    R.layout.screen_launch
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<OnBoardingViewModel>()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                delay(1600)
                val nav = Navigation.findNavController(
                    view = view
                )
                nav.navigate(R.id.action_navigate_to_welcome_screen)
                viewModel.state.collect {
                    Log.e("state",it.toString())
                    when (it) {
                        0 -> {
                            val dest = nav.currentDestination
                            if (dest != null && dest.label == "launch_screen") {
                                nav.navigate(R.id.action_navigate_to_welcome_screen)
                            }
                        }
                        1 -> {
                            val toMainActivity = Intent(requireActivity(),
                                MainActivity::class.java)
                            startActivity(toMainActivity)
                            requireActivity().finish()
                        }
                    }
                    viewModel.state.cancellable()
                }
            }
        }
    }
}