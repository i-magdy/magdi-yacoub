package org.myf.ahc.feature.registration.util

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.io.FileNotFoundException

open class ActivityLauncherObserver(
    private val registry : ActivityResultRegistry,
    private val listener: IntentLauncherListener
): DefaultLifecycleObserver {

    private lateinit var pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private lateinit var pickFileIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickFileIntent: Intent

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        pickImageIntentLauncher = pickImageLauncher(owner)
        pickFileIntentLauncher = pickFileLauncher(owner)
        pickImageIntent = Intent(Intent.ACTION_PICK)
            .apply {
                type = "image/*"
            }
        pickFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            .apply {
                type = "application/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
    }

    private fun pickImageLauncher(
        owner: LifecycleOwner
    ) = registry.register(
        CALL_IMAGE_KEY+this,
        owner,
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val uri = data.data
                try {
                    uri?.let {
                        Log.e("launcher",it.toString())
                       listener.onImagePicked(uri = it)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun pickFileLauncher(
        owner: LifecycleOwner
    )  = registry.register(
        CALL_FILE_KEY+this,
        owner,
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val uri = data.data
                try {
                    uri?.let {
                        listener.onFilePicked(uri = it)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun pickImage() = pickImageIntentLauncher.launch(pickImageIntent)
    fun pickFile() = pickFileIntentLauncher.launch(pickFileIntent)

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        pickFileIntentLauncher.unregister()
        pickImageIntentLauncher.unregister()
    }

    companion object{
        const val CALL_IMAGE_KEY = "pick an image key"
        const val CALL_FILE_KEY = "pick a file key"
    }
}