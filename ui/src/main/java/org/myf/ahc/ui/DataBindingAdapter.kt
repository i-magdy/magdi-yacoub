package org.myf.ahc.ui

import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.myf.ahc.core.common.util.CreatePatientUiError

@BindingAdapter("app:showCreateError")
fun setCreatePatientError(
    layout: TextInputLayout,
    error: CreatePatientUiError
)= when(error){
    CreatePatientUiError.INVALID_PATIENT_NAME ->
        layout.error = layout.context.getString(R.string.invalid_patient_name)
    CreatePatientUiError.INVALID_NATIONAL_ID ->
        layout.error = layout.context.getString(R.string.invalid_national_id)
    CreatePatientUiError.NONE -> layout.error = null
}

@BindingAdapter("app:showCreateInputText")
fun setCreatePatientInputText(
    editText: TextInputEditText,
    text: String?
){
    if (text != null && text.isNotEmpty()){
        editText.setText(text)
    }
}

@BindingAdapter("app:showImageUri")
fun setImageByUri(
    view: ImageView,
    uri: Uri
) = try {
    if (uri.toString().isNotEmpty()) {
        view.setBackgroundColor(Color.TRANSPARENT)
        view.scaleType = ImageView.ScaleType.CENTER_CROP
        view.setImageURI(uri)
    } else {  }
}catch (e: Exception){
    view.scaleType = ImageView.ScaleType.FIT_CENTER
    view.setImageResource(R.drawable.ic_add_circle)
    e.printStackTrace()
}