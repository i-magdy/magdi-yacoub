package org.myf.demo.feature.healthcare

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.myf.demo.core.common.model.ArticleModel
import org.myf.demo.feature.healthcare.databinding.ArticleListItemBinding

class ArticlesAdapter: RecyclerView.Adapter<ArticlesViewHolder>() {

    private var articles: List<ArticleModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setArticles(articles: List<ArticleModel>){
        this.articles = articles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder = ArticlesViewHolder(
        binding = ArticleListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int  = articles.size

    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) = holder.onBind(articles[position])
}