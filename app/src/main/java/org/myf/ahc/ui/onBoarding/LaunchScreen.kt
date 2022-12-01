package org.myf.ahc.ui.onBoarding

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.R

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class LaunchScreen : Fragment(
    R.layout.screen_launch
)  {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}