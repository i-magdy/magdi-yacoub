package org.myf.demo.feature.registration.ui.verify

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.demo.core.common.util.NetworkUtil.isMobileConnectedToInternet
import org.myf.demo.feature.registration.databinding.ScreenVerifyBinding
import org.myf.demo.feature.registration.util.PhoneHintListener
import org.myf.demo.feature.registration.util.PhoneHintObserver

import org.myf.demo.ui.R as uiResource
import org.myf.demo.feature.registration.R as resource

@AndroidEntryPoint
class VerifyScreen : Fragment() {

    private var _binding: ScreenVerifyBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    private lateinit var phoneObserver: PhoneHintObserver
    private val viewModel by viewModels<VerifyViewModel>()
    private lateinit var lang: String
    private val dialog = VerifySuccessBottomSheet()
    private val args by navArgs<VerifyScreenArgs>()
    private var shouldLogin = false

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shouldLogin = args.isShouldLogin && args.phone.isNotBlank()
        if (shouldLogin){ viewModel.shouldUpdateUiForLogIn() }
        phoneObserver = PhoneHintObserver(
            activity = requireActivity(),
            registry = requireActivity().activityResultRegistry,
            listener = object : PhoneHintListener{
                override fun phoneHint(phone: String) {
                    if (phone.isEmpty()){
                        binding.phoneIl.helperText = getString(uiResource.string.enter_phone_message)
                    }else {
                        viewModel.setPhone(phone)
                    }
                }

            }
        )
        lifecycle.addObserver(phoneObserver)
        val phone = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (isMobileConnectedToInternet(requireContext())){
            var code: String? = phone.simCountryIso
            code = code ?: phone.networkCountryIso
            if (!shouldLogin && auth.currentUser == null) {
                viewModel.getCountries()
                viewModel.getCountryCode(code ?: "eg")
            }
        }else{
            Toast.makeText(context,getString(uiResource.string.offline_message),Toast.LENGTH_LONG).show()
        }
        lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0].language
        }else{
            resources.configuration.locale.language
        }
        auth.setLanguageCode(lang)
        viewModel.init(
            lang = lang,
            builder = PhoneAuthOptions.newBuilder(auth).setActivity(requireActivity())
        )
        if (shouldLogin){
            if (auth.currentUser == null) {
                viewModel.setPhone(args.phone)
                viewModel.requestCode()
            }else{
                viewModel.succeed(
                    isSucceed = auth.currentUser != null,
                    forLogin = shouldLogin
                )
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ScreenVerifyBinding.inflate(layoutInflater,container,false)
        val behavior = BottomSheetBehavior.from(binding.relativePhoneBottomSheet)
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        updateUi()
        binding.phoneEt.doAfterTextChanged {
            viewModel.setPhone(it?.toString() ?: "")
            binding.phoneIl.helperText = null
            viewModel.clearError()
        }
        binding.verifyCodeEt.doAfterTextChanged {
            binding.code = it?.toString() ?: ""
            viewModel.clearError()
        }
        binding.countriesAc.doAfterTextChanged {
            val name: String = it?.toString() ?: ""
            if (name.isNotBlank()) {
                viewModel.setCountry(name)
            }
            viewModel.clearError()
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.uiState
                        .map { it.isSuccess }
                        .distinctUntilChanged()
                        .collect {
                            if (it) { showBottomSheet() }
                        }
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(){
        if (shouldLogin){
            val n = args.phone.subSequence(args.phone.length-2,args.phone.length)
            binding.verifyLogInMessage.text = "${getString(uiResource.string.verify_log_in_message)}: ****$n"
            val constrain = ConstraintSet()
            constrain.clone(binding.verifyConstraintLayout)
            constrain.connect(binding.verifyCodeIl.id,
                ConstraintSet.TOP,
                binding.verifyLogInMessage.id,
                ConstraintSet.BOTTOM,32
            )
            constrain.connect(binding.verifyCodeIl.id,
                ConstraintSet.START,
                ConstraintSet.PARENT_ID,
                ConstraintSet.START,16
            )
            constrain.connect(binding.verifyCodeIl.id,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,16
            )
            constrain.applyTo(binding.verifyConstraintLayout)
        }
    }

    private fun showBottomSheet(){
        if (dialog.isAdded) return
        if (shouldLogin){
           Navigation.findNavController(requireView()).navigate(resource.id.action_navigate_from_verify_to_profile)
        }else {
            requireView().findFragment<VerifyScreen>().let {
                dialog.show(it.childFragmentManager, "success_dialog")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            viewModel.setPhone(auth.currentUser?.phoneNumber ?: "")
            viewModel.succeed(
                isSucceed = true,
                forLogin = shouldLogin
            )
            viewModel.savePatient(
                verified = auth.currentUser != null,
                phone = auth.currentUser?.phoneNumber ?: ""
            )
        }else {
            val p: String = binding.phoneEt.editableText.toString()
            if (p.isBlank()) {
                if (!shouldLogin) {
                    phoneObserver.requestPhone()
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}