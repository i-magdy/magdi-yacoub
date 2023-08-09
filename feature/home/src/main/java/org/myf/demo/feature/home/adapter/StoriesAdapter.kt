package org.myf.demo.feature.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import org.myf.demo.core.common.model.StoryModel
import org.myf.demo.feature.home.databinding.StoryListItemBinding

class StoriesAdapter: Adapter<StoriesViewHolder>() {

    private var stories: List<StoryModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setStories(stories: List<StoryModel>){
        this.stories = stories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder = StoriesViewHolder(
        binding = StoryListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int =  stories.size

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.onBind(stories[position])
    }
}