package org.myf.ahc.feature.registration.util

import android.net.Uri

interface IntentLauncherListener {

    fun onImagePicked(uri: Uri)

    fun onFilePicked(uri: Uri)
}