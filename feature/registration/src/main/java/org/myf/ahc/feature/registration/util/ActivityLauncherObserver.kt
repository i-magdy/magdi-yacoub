package org.myf.ahc.feature.registration.util

import android.app.Activity
import android.content.Intent
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

    private lateinit var owner: LifecycleOwner
    private var _pickImageIntentLauncher: ActivityResultLauncher<Intent>? = null
    private var _pickFileIntentLauncher: ActivityResultLauncher<Intent>? = null
    private val pickImageIntentLauncher: ActivityResultLauncher<Intent>
        get(){
            if (_pickImageIntentLauncher == null && this::owner.isInitialized){
                _pickImageIntentLauncher = pickImageLauncher(owner)
            }
            return _pickImageIntentLauncher ?: throw AssertionError("set pick image launcher null")
        }
    private val pickFileIntentLauncher: ActivityResultLauncher<Intent>
        get() {
            if (_pickFileIntentLauncher == null && this::owner.isInitialized){
                _pickFileIntentLauncher = pickFileLauncher(owner)
            }
            return _pickFileIntentLauncher ?: throw AssertionError("set pick file launcher null")
        }
    private lateinit var pickImageIntent: Intent
    private lateinit var pickFileIntent: Intent

    private var INSTANCE = 0 //!! instance

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        this@ActivityLauncherObserver.owner = owner
        INSTANCE++
        _pickImageIntentLauncher = pickImageLauncher(owner)
        _pickFileIntentLauncher = pickFileLauncher(owner)
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
        CALL_IMAGE_KEY+INSTANCE,
        owner,
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val uri = data.data
                try {
                    uri?.let {
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
        CALL_FILE_KEY+INSTANCE,
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
        _pickImageIntentLauncher = null
        _pickFileIntentLauncher = null
    }

    companion object{
        const val CALL_IMAGE_KEY = "pick an image key"
        const val CALL_FILE_KEY = "pick a file key"
    }
}