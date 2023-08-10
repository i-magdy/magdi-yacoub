package org.myf.demo.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.myf.demo.core.common.util.CreatePatientUiError
import org.myf.demo.core.common.util.VerifyUiError
import org.myf.demo.core.model.countries.CountryCodeModel
import java.io.FileNotFoundException
import java.io.InputStream

@BindingAdapter("app:showNameError")
fun setCreatePatientError(
    layout: TextInputLayout,
    error: CreatePatientUiError
)= when(error){
    CreatePatientUiError.EMPTY_NAME ->
        layout.error = layout.context.getString(R.string.empty_field_message)
    CreatePatientUiError.INVALID_PATIENT_NAME ->
        layout.error = layout.context.getString(R.string.invalid_patient_name)
    CreatePatientUiError.INVALID_NAME_FORMAT ->
        layout.error = layout.context.getString(R.string.invalid_name_format_message)
    CreatePatientUiError.NONE_NAME -> layout.error = null
    else -> {}
}
@BindingAdapter("app:showIdError")
fun setPatientIdError(
    layout: TextInputLayout,
    error: CreatePatientUiError
) = when(error) {
    CreatePatientUiError.EMPTY_ID ->
        layout.error = layout.context.getString(R.string.empty_field_message)
    CreatePatientUiError.INVALID_NATIONAL_ID ->
        layout.error = layout.context.getString(R.string.invalid_national_id)
    CreatePatientUiError.INVALID_ID_FORMAT ->
        layout.error = layout.context.getString(R.string.invalid_id_format_message)
    CreatePatientUiError.NONE_ID -> layout.error = null
    else -> {}
}

@BindingAdapter("app:showEmailError")
fun setPatientEmailError(
    layout: TextInputLayout,
    error: CreatePatientUiError
) = when(error) {
    CreatePatientUiError.INVALID_EMAIL ->
        layout.error = layout.context.getString(R.string.invalid_email_message)
    CreatePatientUiError.NONE_EMAIL -> layout.error = null
    else -> {}
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
    if (uri != Uri.EMPTY) {
        var stream: InputStream? = null
        try {
            stream = view.context.contentResolver.openInputStream(uri)
            view.setBackgroundColor(Color.TRANSPARENT)
            view.scaleType = ImageView.ScaleType.CENTER_CROP
            view.setImageURI(uri)
        }catch (e: FileNotFoundException){
            view.scaleType = ImageView.ScaleType.FIT_CENTER
            view.setImageResource(R.drawable.ic_add_circle)
            e.printStackTrace()
        }finally {
            stream?.close()
        }
    }else{ Log.e("image_uri_binding","URI IS EMPTY") }
}catch (e: Throwable){
    view.scaleType = ImageView.ScaleType.FIT_CENTER
    view.setImageResource(R.drawable.ic_add_circle)
    e.printStackTrace()
}

@BindingAdapter("app:showCountries")
fun setCountriesName(ac: AutoCompleteTextView,countries: List<String>){
    val adapter = ArrayAdapter(
        ac.context,
        android.R.layout.simple_list_item_1, countries
    )
    ac.setAdapter(adapter)
}

@BindingAdapter("app:showVerifyCodeError")
fun setVerifyScreenCodeError(
    layout: TextInputLayout,
    error: VerifyUiError
) = when(error){
    VerifyUiError.INVALID_CODE -> {
        layout.error = layout.context.getString(R.string.wrong_code_message)
    }
    else -> { layout.error = null }
}

@BindingAdapter("app:showCountriesError")
fun setVerifyScreenCountriesError(
    layout: TextInputLayout,
    error: VerifyUiError
) = when(error){
    VerifyUiError.SELECT_COUNTRY -> {
        layout.error = layout.context.getString(R.string.select_country_message)
    }
    else -> { layout.error = null }
}

@BindingAdapter("app:showVerifyPhoneError")
fun setVerifyScreenPhoneError(
    layout: TextInputLayout,
    error: VerifyUiError
) = when(error){
    VerifyUiError.INVALID_PHONE -> {
        layout.error = layout.context.getString(R.string.wrong_phone_message)
    }
    else -> { layout.error = null }
}

@Suppress("DEPRECATION")
@BindingAdapter("app:countryName")
fun setCountryName(
    ac: AutoCompleteTextView,
    country: CountryCodeModel
){
    val lang = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        ac.context.resources.configuration.locales[0].language
    }else{
        ac.context.resources.configuration.locale.language
    }
    if (lang == "ar"){
        ac.setText(country.ar_name)
    }else{
        ac.setText(country.en_name)
    }
}

@BindingAdapter("app:setImageUrl")
fun setImageUrl(
    imageView: ImageView,
    url: String
){
    Glide.with(imageView)
        .load(url)
        .into(imageView)
}

@BindingAdapter("app:openWebUrl")
fun openWebUrl(button: MaterialButton,url: String){
    button.setOnClickListener {
        if (url.isEmpty()){
            Toast.makeText(
                button.context,
                "Something went wrong!",
                Toast.LENGTH_SHORT
            ).show()
        }else {
            try {
                button.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}