package org.myf.demo.ui.onBoarding

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.R
import org.myf.demo.ui.main.MainActivity

@AndroidEntryPoint
class NotifyScreen: Fragment(
    R.layout.screen_notify
) {
    private val viewModel by viewModels<OnBoardingViewModel>()
    private val requestPermissionLauncher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        viewModel.updateState(2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.notify_am_in_button)
            .setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (checkSelfPermission(requireActivity(),Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }else{
                    viewModel.updateState(2)
                }
            }

        view.findViewById<MaterialButton>(R.id.notify_skip_button)
            .apply {
                visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) View.VISIBLE else View.GONE
            }.setOnClickListener {
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