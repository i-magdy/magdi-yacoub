package org.myf.ahc.feature.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.feature.home.databinding.DialogSettingsBinding
import org.myf.ahc.ui.R

@AndroidEntryPoint
class SettingsDialog : DialogFragment() {

    private var _binding: DialogSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<SettingsViewModel>()
        val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        }else{
            resources.configuration.locale.language
        }
        if (lang == "ar"){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.arabicButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_100,null))
                binding.arabicButton.setTextColor(resources.getColor(R.color.purple_700,null))
            }else{
                binding.arabicButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.purple_100))
                binding.arabicButton.setTextColor(resources.getColor(R.color.purple_700))
            }
            binding.arabicButton.strokeWidth = 3
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                binding.englishButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.purple_100, null))
                binding.englishButton.setTextColor(resources.getColor(R.color.purple_700, null))
            }else{
                binding.englishButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.purple_100))
                binding.englishButton.setTextColor(resources.getColor(R.color.purple_700))
            }
            binding.englishButton.strokeWidth = 3
        }
        binding.arabicButton.setOnClickListener { viewModel.updateAppLanguage("ar") }
        binding.englishButton.setOnClickListener { viewModel.updateAppLanguage("en") }
        binding.settingsOkButton.setOnClickListener { dismiss() }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogSettingsBinding.inflate(layoutInflater)
        return AlertDialog
            .Builder(requireContext())
            .setView(binding.root)
            .create()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}