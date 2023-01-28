package org.myf.ahc.feature.registration.ui.createPatient

import android.net.Uri
import org.myf.ahc.feature.registration.util.IntentLauncherListener

interface CreateLauncherListener: IntentLauncherListener {
    override fun onImagePicked(uri: Uri)
    override fun onFilePicked(uri: Uri) {}
}