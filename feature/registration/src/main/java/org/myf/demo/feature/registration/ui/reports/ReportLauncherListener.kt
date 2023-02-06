package org.myf.demo.feature.registration.ui.reports

import android.net.Uri
import org.myf.demo.feature.registration.util.IntentLauncherListener

interface ReportLauncherListener : IntentLauncherListener {
    override fun onImagePicked(uri: Uri)

    override fun onFilePicked(uri: Uri)
}