package org.myf.demo.feature.registration.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.feature.registration.R as resource

@AndroidEntryPoint
class LogInScreen : Fragment(
    resource.layout.screen_log_in
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<LogInViewModel>()
        val idEditText = view.findViewById<TextInputEditText>(resource.id.log_in_id_et)
        view.findViewById<MaterialButton>(resource.id.attempt_log_in_Button)
            .setOnClickListener {
                val id = idEditText.editableText.toString()
                if (id.isNotBlank() && id.length == 14){
                    lifecycleScope.launch {
                        if (viewModel.getPatient(
                            id = id
                        )){
                            viewModel.patient.map {
                                it
                            }.distinctUntilChanged()
                                .collect {
                                    Toast.makeText(context,"True",Toast.LENGTH_SHORT).show()
                                    Toast.makeText(context,it.primaryPhone,Toast.LENGTH_SHORT).show()
                                    if (it.primaryPhone.isNotEmpty()) {
                                        val action = LogInScreenDirections
                                            .actionNavigateFromLogInToVerifyScreen()
                                            .apply {
                                                phone = it.primaryPhone
                                                isShouldLogin = true
                                            }
                                        Navigation.findNavController(view).navigate(action)
                                    }
                                }
                        }else{
                            Toast.makeText(context,"false",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
}