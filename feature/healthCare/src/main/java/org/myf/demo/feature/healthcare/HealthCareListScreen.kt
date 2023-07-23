package org.myf.demo.feature.healthcare

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HealthCareListScreen : Fragment(
    R.layout.screen_care_list
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel by viewModels<ArticlesViewModel>()
        val progress = view.findViewById<CircularProgressIndicator>(R.id.articles_progress_bar)
        val adapter = ArticlesAdapter()
        view.findViewById<RecyclerView>(R.id.articles_rv).apply {
            this.adapter = adapter
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED){
                viewModel.articles.collect {
                    progress.hide()
                    adapter.setArticles(it)
                }
            }
        }
    }
}