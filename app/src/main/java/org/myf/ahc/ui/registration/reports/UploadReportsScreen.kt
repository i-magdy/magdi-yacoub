package org.myf.ahc.ui.registration.reports

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import org.myf.ahc.R
import java.io.FileNotFoundException

@AndroidEntryPoint
class UploadReportsScreen: Fragment(
    R.layout.screen_upload_reports
) {

    private lateinit var  pickImageIntentLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickImageIntent: Intent
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickImageIntent = Intent(Intent.ACTION_PICK)
            .apply {
                type = "image/*"
            }
        pickImageLauncher()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.upload_button)
            .setOnClickListener {
                pickImageIntentLauncher.launch(pickImageIntent)
            }
        imageView = view.findViewById(R.id.imageView)
    }


    private fun pickImageLauncher(){
        pickImageIntentLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val uri = data.data
                    try {
                        val inputStream = uri?.let {
                            requireActivity().contentResolver.openInputStream(it)
                        }
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        //TODO update ui here e.g. { binding.imageView.setImageBitmap(bitmap) }
                        imageView.setImageBitmap(bitmap)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (this::pickImageIntentLauncher.isInitialized){
            pickImageIntentLauncher.unregister()
        }
    }

}