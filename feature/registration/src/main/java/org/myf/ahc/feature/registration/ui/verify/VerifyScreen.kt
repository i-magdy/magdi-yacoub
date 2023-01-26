package org.myf.ahc.feature.registration.ui.verify

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.myf.ahc.core.common.util.NetworkUtil.isMobileConnectedToInternet
import org.myf.ahc.core.common.util.PhoneAuthErrorMessage
import org.myf.ahc.core.common.util.VerifyUiError
import org.myf.ahc.feature.registration.databinding.ScreenVerifyBinding
import org.myf.ahc.feature.registration.util.PhoneHintListener
import org.myf.ahc.feature.registration.util.PhoneHintObserver

import java.util.concurrent.TimeUnit
import org.myf.ahc.ui.R as uiResource

@AndroidEntryPoint
class VerifyScreen : Fragment() {

    private var _binding: ScreenVerifyBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    private lateinit var options: PhoneAuthOptions.Builder
    private var storedVerificationId: String = ""
    private lateinit var phoneObserver: PhoneHintObserver
    private val viewModel by viewModels<VerifyViewModel>()
    private lateinit var lang: String
    private val dialog = VerifySuccessBottomSheet()
    private val args by navArgs<VerifyScreenArgs>()
    private var shouldLogin = false

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
        viewModel.setAppLang(lang)
        options = PhoneAuthOptions.newBuilder(auth)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(requireActivity())
        if (shouldLogin){
            if (auth.currentUser == null) {
                requestCode(args.phone)
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
                launch {
                    viewModel.phoneToVerify.collect {
                        if (it.isNotBlank()) {
                            requestCode(phone = it)
                        }
                    }
                }
                launch {
                    viewModel.verifyCode.collect {
                        if (it.isNotBlank()) {
                            signInWithPhoneAuthCredential(getCredential(it))
                        }
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
            //TODO navigate when success log in
           /* val toLaunchIntent = Intent(requireActivity(),OnBoardingActivity::class.java)
            startActivity(toLaunchIntent)
            requireActivity().finish()*/
        }else {
            dialog.show(childFragmentManager, "success_dialog")
        }
    }
    private fun requestCode(phone: String){
        options.setPhoneNumber(phone)
        options.setCallbacks(callbacks)
        PhoneAuthProvider.verifyPhoneNumber(options.build())
        viewModel.codeRequested()
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

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        if (auth.currentUser != null) return
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    viewModel.succeed(
                        isSucceed = user != null,
                        forLogin = shouldLogin
                    )
                    viewModel.savePatient(
                        verified = user != null,
                        phone = user?.phoneNumber ?: ""
                    )
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        viewModel.wrongCode()
                    }
                    // Update UI
                }
            }
    }

    private val callbacks get() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("onVerificationFailed", "*${e.localizedMessage}*")
            if (e.localizedMessage == PhoneAuthErrorMessage.invalid_phone){ viewModel.wrongPhone() }
            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            }

            // Show a message and update the UI
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            Log.e("time_out",p0)
        }


        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")
            viewModel.codeSent(true)
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            //resendToken = token
        }
    }

    private fun getCredential(
        code: String
    ): PhoneAuthCredential  = PhoneAuthProvider.getCredential(storedVerificationId, code)

}