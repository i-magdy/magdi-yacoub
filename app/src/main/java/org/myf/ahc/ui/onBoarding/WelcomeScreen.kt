package org.myf.ahc.ui.onBoarding

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
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
import org.myf.ahc.R
import org.myf.ahc.ui.main.MainActivity
import org.myf.ahc.ui.R as uiResource

@AndroidEntryPoint
class WelcomeScreen: Fragment(
    R.layout.screen_welcome
){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<OnBoardingViewModel>()
        val arabicButton = view.findViewById<MaterialButton>(R.id.arabic_button)
        val englishButton = view.findViewById<MaterialButton>(R.id.english_button)

        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        }else{
            resources.configuration.locale.language
        }
        if (lang == "ar"){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                arabicButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(uiResource.color.purple_100,null))
                arabicButton.setTextColor(resources.getColor(uiResource.color.purple_700,null))
            }else{
                arabicButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(uiResource.color.purple_100))
                arabicButton.setTextColor(resources.getColor(uiResource.color.purple_700))
            }
            arabicButton.strokeWidth = 3
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                englishButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(uiResource.color.purple_100, null))
                englishButton.setTextColor(resources.getColor(uiResource.color.purple_700, null))
            }else{
                englishButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(uiResource.color.purple_100))
                englishButton.setTextColor(resources.getColor(uiResource.color.purple_700))
            }
            englishButton.strokeWidth = 3
        }
        arabicButton.setOnClickListener { viewModel.updateAppLanguage("ar") }
        englishButton.setOnClickListener { viewModel.updateAppLanguage("en") }
        view.findViewById<MaterialButton>(R.id.start_button).setOnClickListener {
            viewModel.updateState(1)
        }
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