package org.myf.ahc.feature.registration.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.feature.registration.R
import org.myf.ahc.feature.registration.util.FileTypesUtil
import org.myf.ahc.feature.registration.util.FilesSizeUtil


class ReportsAdapter(
    private val listener: ReportAdapterListener
): RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>() {

    private var list: List<DocumentModel> = emptyList()
    inner class ReportsViewHolder(
        val view: View
    ): RecyclerView.ViewHolder(view), View.OnClickListener{

        private val name = view.findViewById<TextView>(R.id.report_item_name_tv)
        private val img = view.findViewById<ImageView>(R.id.report_item_type_iv)
        private val size = itemView.findViewById<TextView>(R.id.report_item_size_tv)
        private val editIv = itemView.findViewById<ImageView>(R.id.report_item_edit_iv)
        private val note = itemView.findViewById<TextView>(R.id.report_item_note_tv)

        init {
            editIv.setOnClickListener(this)
        }

        fun onBind(document: DocumentModel){
            name.text = document.name
            size.text = FilesSizeUtil.getSize(document.size)
            note.text = document.note
            when(document.type){
                FileTypesUtil.MICROSOFT_WORD -> img.setImageResource(R.drawable.word)
                FileTypesUtil.PDF -> img.setImageResource(R.drawable.pdf)
                else -> img.setImageResource(R.drawable.ic_photo)
            }
        }

        override fun onClick(v: View?) {
            listener.onEditFile(path = list[adapterPosition].path)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setDocuments(list: List<DocumentModel>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder = ReportsViewHolder(
        view = LayoutInflater.from(parent.context).inflate(R.layout.report_list_item,parent,false)
    )

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int)  = holder.onBind(document = list[position])


    override fun getItemCount(): Int = list.size

    interface ReportAdapterListener{
        fun onEditFile(path: String)
    }
}