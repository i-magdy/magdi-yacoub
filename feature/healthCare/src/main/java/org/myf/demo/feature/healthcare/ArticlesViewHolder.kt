package org.myf.demo.feature.healthcare

import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import org.myf.demo.core.common.model.ArticleModel
import org.myf.demo.feature.healthcare.databinding.ArticleListItemBinding

class ArticlesViewHolder(
    private val binding: ArticleListItemBinding
): RecyclerView.ViewHolder(binding.root){

    init {
        binding.setArticleClickListener { _ ->
            binding.article?.let {
                val action = HealthCareListScreenDirections.actionReadArticle().apply {
                    url = it.url
                }
                Navigation.findNavController(itemView).navigate(action)
            }
        }
    }

    fun onBind(article: ArticleModel){
        binding.article = article
        binding.executePendingBindings()
    }
}