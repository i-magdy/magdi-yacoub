package org.myf.ahc.feature.registration.adapters

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import org.myf.ahc.core.model.storage.DocumentModel
import org.myf.ahc.feature.registration.databinding.ReportListItemBinding
import org.myf.ahc.feature.registration.ui.reports.EditReportDialog
import org.myf.ahc.feature.registration.ui.reports.EditReportDialog.Companion.PATH_KEY
import org.myf.ahc.feature.registration.ui.reports.EditReportDialog.Companion.tag

class DocumentsViewHolder(
    private val binding: ReportListItemBinding,
    private val fragmentManager: FragmentManager
): RecyclerView.ViewHolder(binding.root) {
    private val editDialog = EditReportDialog()
    init {
        binding.setEditDocumentClickListener { _ ->
            binding.document?.let {
                if (editDialog.isAdded) return@setEditDocumentClickListener
                editDialog.arguments = Bundle().apply {
                    putString(PATH_KEY,it.path)
                }
                editDialog.show(fragmentManager,tag)
            }
        }
    }

    fun onBind(
        document: DocumentModel
    ){
        binding.document = document
        binding.executePendingBindings()
    }
}