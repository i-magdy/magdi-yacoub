package org.myf.demo.core.data.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import org.myf.demo.core.common.annotation.Dispatcher
import org.myf.demo.core.common.annotation.MyDispatchers.IO
import org.myf.demo.core.common.uiState.VerificationServiceState
import org.myf.demo.core.common.util.PhoneAuthErrorMessage
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VerificationServiceRepositoryImpl @Inject constructor(
    @Dispatcher(IO) private val dispatcher: CoroutineDispatcher
): VerificationServiceRepository {

    private val auth = Firebase.auth
    private lateinit var options: PhoneAuthOptions.Builder
    private lateinit var credential: PhoneAuthCredential
    private var storedVerificationId: String = ""
    override val state: MutableStateFlow<VerificationServiceState> = MutableStateFlow(
        VerificationServiceState()
    )

    override fun initService(
        appLanguage: String,
        builder: PhoneAuthOptions.Builder
    ) {
        options = builder.setTimeout(60L, TimeUnit.SECONDS)
    }

    private var updateJob: Job? = null
    private val coroutine = CoroutineScope(dispatcher + SupervisorJob())
    private val callbacks get() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            this@VerificationServiceRepositoryImpl.credential = credential
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(ContentValues.TAG, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("onVerificationFailed", "*${e.localizedMessage}*")
            if (e.localizedMessage == PhoneAuthErrorMessage.invalid_phone){
                updateJob?.cancel()
                updateJob = coroutine.launch {
                    state.getAndUpdate { s ->
                        s.copy(wrongPhone = true)
                    }
                }
            }
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
            Log.d(ContentValues.TAG, "onCodeSent:$verificationId")
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            //resendToken = token
            updateJob?.cancel()
            updateJob = coroutine.launch {
                state.getAndUpdate {
                    it.copy(codeSent = true)
                }
            }
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        if (auth.currentUser != null) return
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    updateJob?.cancel()
                    updateJob = coroutine.launch {
                        state.getAndUpdate {
                            it.copy(isSucceed = user != null)
                        }
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        updateJob?.cancel()
                        updateJob = coroutine.launch {
                            state.getAndUpdate { s ->
                                s.copy(wrongCode = true)
                            }
                        }
                    }
                    // Update UI
                }
            }
    }

    override fun requestCode(
        phone: String
    ){
        if (!this::options.isInitialized){ return }
        options.setPhoneNumber(phone)
        options.setCallbacks(callbacks)
        PhoneAuthProvider.verifyPhoneNumber(options.build())
        updateJob?.cancel()
        updateJob = coroutine.launch {
            state.getAndUpdate {
                it.copy(codeRequested = true)
            }
        }
    }

    override fun verify(
        code: String
    ){
        credential  = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }


    override fun cancelJob() {
        coroutine.coroutineContext.cancelChildren()
    }
}