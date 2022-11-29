package org.myf.ahc.ui.registration.verify

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.myf.ahc.databinding.FragmentVerifyBinding
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class VerifyFragment : Fragment() {

    private var _binding: FragmentVerifyBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth
    private lateinit var options: PhoneAuthOptions.Builder
    private var storedVerificationId: String = ""
    private lateinit var phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private val viewModel by viewModels<VerifyViewModel>()
    private lateinit var lang: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val phone = requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        viewModel.getCountryCode(phone.networkCountryIso)
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
        phoneNumberHintIntentResultLauncher = preparePhoneHintLauncher()
        requestPhoneNumberHint()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyBinding.inflate(layoutInflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.phoneEt.doAfterTextChanged { viewModel.setPhone(it?.toString() ?: "") }
        binding.verifyCodeEt.doAfterTextChanged { binding.code = it?.toString() ?: "" }
        binding.countriesAc.doAfterTextChanged {
            val name: String = it?.toString() ?: ""
            if (name.isNotBlank()){
                viewModel.setCountry(name)
            }
        }

        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.selectedCountry.collect{
                    viewModel.filterPhone()
                    if (lang == "ar"){
                        binding.countriesAc.setText(it.ar_name)
                    }else{
                        binding.countriesAc.setText(it.en_name)
                    }
                }
            }
            launch {
                viewModel.countriesName.collect{
                    val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1,it)
                    binding.countriesAc.setAdapter(adapter)
                }
            }

            launch { viewModel.phoneToVerify.collect{
                if (it.isNotBlank()){
                    requestCode(phone = it)
                }
            } }

            launch {
                viewModel.verifyCode.collect{
                    if (it.isNotBlank()){
                        signInWithPhoneAuthCredential(getCredential(it))
                    }
                }
            }
        }
    }

    private fun requestCode(phone: String){
        options.setPhoneNumber(phone)
        options.setCallbacks(callbacks)
        PhoneAuthProvider.verifyPhoneNumber(options.build())
        viewModel.codeRequested()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        phoneNumberHintIntentResultLauncher.unregister()
    }

    private fun preparePhoneHintLauncher(): ActivityResultLauncher<IntentSenderRequest> {
        return registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val phoneNumber = Identity.getSignInClient(requireActivity())
                        .getPhoneNumberFromIntent(result.data)
                    println("phoneNumber $phoneNumber")
                    viewModel.setPhone(phoneNumber)
                } catch (e: Exception) {
                    println("Phone Number Hint failed")
                    e.printStackTrace()
                }
            }else{
                Log.e("phone","cancelled")
            }
        }
    }

    private fun requestPhoneNumberHint(){
        val request: GetPhoneNumberHintIntentRequest =
            GetPhoneNumberHintIntentRequest.builder().build()
        Identity.getSignInClient(requireActivity())
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
                try {
                    phoneNumberHintIntentResultLauncher.launch(IntentSenderRequest.Builder(
                        it.intentSender
                    ).build())
                } catch(e: Exception) {
                    Log.e(TAG, "Launching the PendingIntent failed")
                }
            }.addOnFailureListener {
                Log.e(TAG, "${it.message}")
            }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user

                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
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
            Log.w(TAG, "onVerificationFailed", e)
            viewModel.codeSent(false)
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