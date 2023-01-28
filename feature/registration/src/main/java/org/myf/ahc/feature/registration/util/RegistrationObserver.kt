package org.myf.ahc.feature.registration.util

import android.net.Uri
import androidx.lifecycle.DefaultLifecycleObserver

interface RegistrationObserver: DefaultLifecycleObserver {

    fun pickImage(){}
    fun pickFile(){}
    fun requestPhone(){}
    fun onImagePicked(uri: Uri){}
    fun onFilePicked(uri: Uri){}
    fun onPhoneRequested(phone: String){}

}