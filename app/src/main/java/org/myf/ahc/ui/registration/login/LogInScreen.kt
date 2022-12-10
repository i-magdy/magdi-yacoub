package org.myf.ahc.ui.registration.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import org.myf.ahc.R as resource

class LogInScreen : Fragment(
    resource.layout.screen_log_in
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(resource.id.attempt_log_in_Button)
            .setOnClickListener {
                val action = LogInScreenDirections.actionNavigateFromLogInToVerifyScreen()
                    .apply {
                        phone = "+201234567890"
                        isShouldLogin = true
                    }
                Navigation.findNavController(view).navigate(action)
            }
    }
}