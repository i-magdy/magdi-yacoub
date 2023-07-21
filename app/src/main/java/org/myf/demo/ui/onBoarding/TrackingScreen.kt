package org.myf.demo.ui.onBoarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.myf.demo.R

@AndroidEntryPoint
class TrackingScreen: Fragment(
    R.layout.screen_tracking
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.tracking_next_button)
            .setOnClickListener {
                findNavController().navigate(R.id.action_navigate_to_security_screen)
            }
        view.findViewById<MaterialButton>(R.id.tracking_pervious_button)
            .setOnClickListener {
                findNavController().popBackStack()
            }
    }
}