package org.myf.ahc.feature.registration.util

import android.net.Uri

interface CreateLauncherListener: IntentLauncherListener {
    override fun onImagePicked(uri: Uri)
    override fun onFilePicked(uri: Uri) {}
}