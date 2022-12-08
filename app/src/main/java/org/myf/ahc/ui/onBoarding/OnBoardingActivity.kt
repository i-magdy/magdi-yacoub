package org.myf.ahc.ui.onBoarding


import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import org.myf.ahc.R
import org.myf.ahc.ui.BaseActivity
import org.myf.ahc.ui.registration.RegistrationActivity

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity(
    R.layout.activity_on_boarding
) {

    val viewModel by viewModels<OnBoardingViewModel>()
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(1600)
            val nav =  Navigation.findNavController(
            activity = this@OnBoardingActivity,
            viewId = R.id.on_boarding_nav_host_fragment
            )
            viewModel.state.collect{
                when(it){
                    0 -> {
                        val dest = nav.currentDestination
                        if (dest != null && dest.label == "launch_screen") {
                            nav.navigate(R.id.action_navigate_to_welcome_screen)
                        }
                    }
                    else -> {
                        val toRegistration = Intent(this@OnBoardingActivity,RegistrationActivity::class.java)
                        startActivity(toRegistration)
                        finish()
                    }
                }
                viewModel.state.cancellable()
            }
        }
    }
}