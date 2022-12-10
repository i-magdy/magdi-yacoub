package org.myf.ahc.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.myf.ahc.R
import org.myf.ahc.models.DocumentModel
import org.myf.ahc.util.FileTypesUtil
import org.myf.ahc.util.FilesSizeUtil

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
        private val deleteIv = itemView.findViewById<ImageView>(R.id.report_item_remove_iv)

        init {
            deleteIv.setOnClickListener(this)
        }

        fun onBind(document: DocumentModel){
            name.text = document.name
            size.text = FilesSizeUtil.getSize(document.size)
            when(document.type){
                FileTypesUtil.MICROSOFT_WORD -> img.setImageResource(R.drawable.word)
                FileTypesUtil.PDF -> img.setImageResource(R.drawable.pdf)
                else -> img.setImageResource(R.drawable.ic_photo)
            }
        }

        override fun onClick(v: View?) {
            listener.onDeleteFile(path = list[adapterPosition].path)
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
        fun onDeleteFile(path: String)
    }
}