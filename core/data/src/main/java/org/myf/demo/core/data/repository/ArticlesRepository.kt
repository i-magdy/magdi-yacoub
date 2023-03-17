package org.myf.demo.core.data.repository

import kotlinx.coroutines.flow.Flow
import org.myf.demo.core.common.model.ArticleModel

interface ArticlesRepository {
    fun getArticles(): Flow<List<ArticleModel>>
    fun cancelJob()
}