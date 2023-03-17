package org.myf.demo.feature.healthcare

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.myf.demo.core.data.repository.ArticlesRepository
import org.myf.demo.core.common.model.ArticleModel
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val repo: ArticlesRepository
): ViewModel(){

    val articles: Flow<List<ArticleModel>> = repo.getArticles()

    override fun onCleared() {
        super.onCleared()
        repo.cancelJob()
    }
}