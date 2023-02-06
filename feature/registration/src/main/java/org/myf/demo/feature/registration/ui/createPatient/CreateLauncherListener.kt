package org.myf.demo.feature.registration.ui.createPatient

import android.net.Uri
import org.myf.demo.feature.registration.util.IntentLauncherListener

interface CreateLauncherListener: IntentLauncherListener {
    override fun onImagePicked(uri: Uri)
    override fun onFilePicked(uri: Uri) {}
}