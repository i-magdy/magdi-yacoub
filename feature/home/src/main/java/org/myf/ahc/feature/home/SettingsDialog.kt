package org.myf.ahc.feature.home

import android.app.AlertDialog
import android.app.Dialog
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
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_view)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<SettingsViewModel>()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
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