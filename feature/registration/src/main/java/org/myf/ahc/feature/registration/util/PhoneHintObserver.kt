package org.myf.ahc.feature.registration.util

import android.app.Activity
import android.content.ContentValues
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest
import com.google.android.gms.auth.api.identity.Identity

class PhoneHintObserver(
    private val activity: Activity,
    private val registry : ActivityResultRegistry,
    private val listener: PhoneHintListener
): RegistrationObserver {

    private lateinit var owner: LifecycleOwner
    private var _phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest>? = null
    private val phoneNumberHintIntentResultLauncher: ActivityResultLauncher<IntentSenderRequest>
        get() = if (_phoneNumberHintIntentResultLauncher == null && this::owner.isInitialized){
            preparePhoneHintLauncher(owner)
        }else {
            _phoneNumberHintIntentResultLauncher ?: throw  AssertionError("Set Phone Hint Launcher")
        }


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        this@PhoneHintObserver.owner = owner
        _phoneNumberHintIntentResultLauncher = preparePhoneHintLauncher(owner)
    }

    private fun preparePhoneHintLauncher(
        owner: LifecycleOwner
    ): ActivityResultLauncher<IntentSenderRequest> {
        return registry.register(
            "phone",
            owner,
            ActivityResultContracts.StartIntentSenderForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val phoneNumber = Identity.getSignInClient(activity)
                        .getPhoneNumberFromIntent(result.data)
                    println("phoneNumber $phoneNumber")
                    onPhoneRequested(phoneNumber)
                } catch (e: Exception) {
                    println("Phone Number Hint failed")
                    e.printStackTrace()
                }
            }else{
                Log.e("phone","cancelled")
                onPhoneRequested("")
            }
        }
    }


    override fun requestPhone(){
        requestPhoneNumberHint()
    }

    override fun onPhoneRequested(phone: String) {
        super.onPhoneRequested(phone)
        listener.phoneHint(phone)
    }

    private fun requestPhoneNumberHint(){
        val request: GetPhoneNumberHintIntentRequest =
            GetPhoneNumberHintIntentRequest.builder().build()
        Identity.getSignInClient(activity)
            .getPhoneNumberHintIntent(request)
            .addOnSuccessListener {
                try {
                    phoneNumberHintIntentResultLauncher.launch(
                        IntentSenderRequest.Builder(it.intentSender).build()
                    )
                } catch(e: Exception) {
                    Log.e(ContentValues.TAG, "Launching the PendingIntent failed")
                }
            }.addOnFailureListener {
                Log.e(ContentValues.TAG, "${it.message}")
            }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        phoneNumberHintIntentResultLauncher.unregister()
        _phoneNumberHintIntentResultLauncher = null
    }
}