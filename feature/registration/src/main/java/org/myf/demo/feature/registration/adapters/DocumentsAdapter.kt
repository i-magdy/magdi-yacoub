package org.myf.demo.feature.registration.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import org.myf.demo.core.model.storage.DocumentModel
import org.myf.demo.feature.registration.databinding.ReportListItemBinding
import org.myf.demo.feature.registration.ui.reports.UploadReportsScreen


class DocumentsAdapter: RecyclerView.Adapter<DocumentsViewHolder>() {

    private var list: List<DocumentModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setDocuments(list: List<DocumentModel>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder = DocumentsViewHolder(
        binding = ReportListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        fragmentManager = parent.findFragment<UploadReportsScreen>().childFragmentManager
    )

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int)  = holder.onBind(document = list[position])

    override fun getItemCount(): Int = list.size

}