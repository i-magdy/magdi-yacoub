package org.myf.demo.ui

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.myf.demo.core.common.util.FileTypesUtil
import org.myf.demo.core.common.util.FilesSizeUtil


@BindingAdapter("app:imageDrawable")
fun setImageDrawable(view: ImageView,documentType: String){
    view.setBackgroundColor(Color.TRANSPARENT)
    when(documentType){
        FileTypesUtil.MICROSOFT_WORD -> view.setImageResource(R.drawable.word)
        FileTypesUtil.PDF -> view.setImageResource(R.drawable.pdf)
        else -> view.setImageResource(R.drawable.ic_photo)
    }
}


@BindingAdapter("app:documentSize")
fun setDocumentSizeTitle(
    view: TextView,
    size: Long
){
    view.text = FilesSizeUtil.getSize(
        size = size,
        kb = view.context.getString(R.string.kilo_byte_title),
        mb = view.context.getString(R.string.mega_byte_title)
    )
}


@BindingAdapter("app:hideIfProgressZero")
fun setHideIfProgressZero(view: View,progress: Int){
    if (progress == 0){
        view.visibility = View.GONE
    }else{
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("app:calculateSize")
fun setCalculateSize(text: TextView,size: Long){
    text.text = FilesSizeUtil.calculateSizePercentage(size.toDouble())
}