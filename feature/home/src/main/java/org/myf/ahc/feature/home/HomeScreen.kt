package org.myf.ahc.feature.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

class HomeScreen : Fragment(
    R.layout.screen_home
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("HEy","HOME")
    }
}