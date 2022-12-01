package org.myf.ahc.ui.onBoarding

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.R

@AndroidEntryPoint
class WelcomeScreen: Fragment(
    R.layout.screen_welcome
){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by activityViewModels<OnBoardingViewModel>()
        val arabicButton = view.findViewById<MaterialButton>(R.id.arabic_button)
        val englishButton = view.findViewById<MaterialButton>(R.id.english_button)

        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        }else{
            resources.configuration.locale.language
        }

        if (lang == "ar"){
            arabicButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_100,null))
            arabicButton.setTextColor(resources.getColor(R.color.purple_700,null))
            arabicButton.strokeWidth = 3
        }else{
            englishButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_100,null))
            englishButton.setTextColor(resources.getColor(R.color.purple_700,null))
            englishButton.strokeWidth = 3
        }
        arabicButton.setOnClickListener { viewModel.updateAppLanguage("ar") }
        englishButton.setOnClickListener { viewModel.updateAppLanguage("en") }
        view.findViewById<MaterialButton>(R.id.start_button).setOnClickListener {
            viewModel.updateState(1)
        }
    }
}